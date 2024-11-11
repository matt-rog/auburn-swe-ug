defmodule Extract do

  def hd_char(start, take, image) do
    {out, _exit_code} = System.cmd("hexdump", ["-C", "-s", "#{start}", "-n", "#{take}", image])
    out
  end

  def hd_no_offset(start, take, image) do
    {out, _exit_code} = System.cmd("hexdump", ["-s", "#{start}", "-n", "#{take}", "-ve", "1/1 \"%.2x\"", image])
    out
  end

  def cut_string(string, lengths) do
    Enum.reduce(lengths, {string, []}, fn length, {str, acc} ->
      {cut, remainder} = String.split_at(str, length)
      {remainder, [cut | acc]}
    end)
    |> elem(1)
    |> Enum.reverse()
  end

  def htoi_be(be_hex_str) do
    be_hex_str
    |> String.split("", trim: true)
    |> Enum.chunk_every(2)
    |> Enum.map(&Enum.join/1)
    |> Enum.reverse()
    |> Enum.join("")
    |> String.to_integer(16)
  end

  def htoi_le(le_hex_str) do
    le_hex_str
    |> String.split("", trim: true)
    |> Enum.chunk_every(2)
    |> Enum.map(&Enum.join/1)
    |> Enum.join("")
    |> String.to_integer(16)
  end

  def htoa(hex_str) do
    hex_str
    |> String.trim()
    |> String.to_charlist()
    |> Enum.chunk_every(2)
    |> Enum.map(&Integer.parse(List.to_string(&1), 16))
    |> Enum.map(fn {num, _} -> num end)
    |> Enum.map(&<<&1::utf8>>)
    |> List.to_string()
  end

  @file_statuses %{
    "00" => "Filename Never Used",
    "e5" => "Filename Used, But Deleted",
    "2e" => "Directory",
    "41" => "Normal File"
  }

  @file_attributes %{
    "01" => "Read Only",
    "02" => "Hidden",
    "04" => "System",
    "08" => "Volume Label",
    "10" => "Directory",
    "20" => "Archive",
    "0F" => "Long File Name",
  }

  def run do
    image = System.argv() |> hd
    {fdisk_output, _exit_code} = System.cmd("fdisk", ["-l", image])
    IO.puts(fdisk_output)
    [partition_start_s, _partition_end_s] = fdisk_output
      |> String.split("\n")
      |> Enum.take(-2)
      |> hd
      |> String.replace(~r/\s+/, ":")
      |> String.split(":")
      |> tl
      |> Enum.take(2)
      |> Enum.map(fn s -> Integer.parse(s) |> elem(0) end)

    partition_start = partition_start_s * 512

    hd_output = hd_char(partition_start_s * 512, 512, image)
    sectors_per_cluster = hd_no_offset(partition_start + 13, 1, image) |> htoi_be
    reserved_sectors = hd_no_offset(partition_start + 14, 2, image) |> htoi_be
    num_fats = hd_no_offset(partition_start + 16, 1, image) |> htoi_be
    sectors_per_fat = hd_no_offset(partition_start + 22, 2, image) |> htoi_be
    sectors_before_partition = hd_no_offset(partition_start + 28, 4, image) |> htoi_be

    IO.puts("sect per clust #{sectors_per_cluster}")
    IO.puts("reserved_sectors #{reserved_sectors}")
    IO.puts("num_fats #{num_fats}")
    IO.puts("sectors_per_fat #{sectors_per_fat}")
    IO.puts("sectors_before_partition #{sectors_before_partition}")


    fat_start = partition_start + (sectors_before_partition * 512) + (reserved_sectors * 512)
    IO.puts("fat start #{fat_start/512}")
    fat = hd_char(fat_start, sectors_per_fat * 512, image)
    fat_clusters = hd_no_offset(fat_start, (sectors_per_fat * 512), image)
      |> String.split(~r/.{1,4}/, trim: true, include_captures: true)
    IO.puts("num clust #{Enum.count(fat_clusters)}")
    IO.puts(fat)

    IO.puts("Root Directory")
    root_dir_start = fat_start + (2 * sectors_per_fat * 512)
    IO.puts("root #{root_dir_start/512}")
    root_dir = hd_char(root_dir_start, 32*512, image)
    root_dir_entries = hd_no_offset(root_dir_start, 32*512, image)
      |> String.split(~r/.{1,128}/, trim: true, include_captures: true)
      |> Enum.reject(fn x -> x == String.duplicate("0", 128) end)

    IO.puts(root_dir)
    Enum.each(root_dir_entries, fn entry ->
      [status, _, name, ext, attr, reserved, time, date, cluster, size] = cut_string(entry, [2, 62, 16, 6, 2, 20, 4, 4, 4, 8])

      IO.puts("Status: #{status} => #{@file_statuses[status]}")
      IO.puts("Name: #{name} => #{htoa(name)}")
      IO.puts("Extention: #{ext} => #{htoa(ext)}")
      IO.puts("Attribute: #{attr} => #{@file_attributes[attr]}")
      IO.puts("Reserved: #{reserved}")
      # IO.puts("#{time}: #{hex_to_time(time)}")
      # IO.puts("#{date}: #{hex_to_date(date)}")
      IO.puts("Cluster: #{cluster} => #{htoi_be(cluster)}")
      IO.puts("Size: #{size} => #{htoi_be(size)} bytes")

      file_sector_count = ceil(htoi_be(size)/512)
      data_area_start = partition_start_s + sectors_before_partition + reserved_sectors + (num_fats * sectors_per_fat) + (32)
      IO.puts("data_area_start #{data_area_start}")
      sector_start = data_area_start + ((htoi_be(cluster) - 2) * sectors_per_cluster)
      IO.puts("sectorstart #{sector_start}")
      IO.puts(file_sector_count)
      {_dd_output, _exit_code} = System.cmd("dd", ["if=#{image}", "of=out/#{String.trim(htoa(name))}.#{String.downcase(htoa(ext))}", "bs=512", "skip=#{sector_start}","count=#{trunc(file_sector_count)}"])







      IO.puts("\n")

    end)



  end
end

Extract.run()
