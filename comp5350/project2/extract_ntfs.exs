defmodule Extract do

  def hd_char(start, take, image) do
    # Calculate how many full 512-byte blocks there are
    start_d = Integer.floor_div(start, 512)
    start_remainder = rem(start, 512)

    take_d = Integer.floor_div(take, 512)
    take_remainder = rem(take, 512)

    # Conditionally construct start expression
    start_expr =
      cond do
        start_d == 0 and start_remainder == 0 -> "0"
        start_d == 0 -> "#{start_remainder}"
        start_remainder == 0 -> "#{start_d} * 512"`
      end

    # Conditionally construct take expression
    take_expr =
      cond do
        take_d == 0 and take_remainder == 0 -> "0"
        take_d == 0 -> "#{take_remainder}"
        take_remainder == 0 -> "#{take_d} * 512"
        true -> "#{take_d} * 512 + #{take_remainder}"
      end

    # Generate the final command string
    cmd = "$ hexdump -C -s $((#{start_expr})) -n $((#{take_expr})) #{image}"

    IO.puts(cmd)
    {out, _exit_code} = System.cmd("hexdump", ["-C", "-s", "#{start}", "-n", "#{take}", image])
    IO.puts(out)
    out
  end

  def hd_no_offset(start, take, image) do
    _cmd = "$ hexdump -C -s #{start} -n #{take} #{image} -ve 1/1 \"%.2x\" #{image}"
    hd_char(start, take, image)
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

  def run do
    image = System.argv() |> hd
    IO.puts("$ fdisk -l #{image}")
    {fdisk_output, _exit_code} = System.cmd("fdisk", ["-l", image])
    IO.puts(fdisk_output)

    spb =
      case Regex.run(~r/sectors of 1 \* (\d+) = \d+ bytes/, fdisk_output) do
        [_, sectors] -> String.to_integer(sectors)
        _ -> nil
    end
    IO.puts("Sectors/byte: #{spb}")

    partition_start_s_l = [2048]
    Enum.each(Enum.with_index(partition_start_s_l), fn {partition_start_s, _i}->

      partition_start = partition_start_s * spb

      # Print MBR
      hd_char(partition_start_s * spb, spb, image)

      IO.puts("\n\nFinding sectors/cluster")
      spc = hd_no_offset(partition_start + 13, 1, image) |> htoi_be
      IO.puts("Sectors/cluster: #{spc}")

      IO.puts("\n\nFinding start of MFT from MBR")
      mft_cluster = hd_no_offset(partition_start + 48, 8, image) |> htoi_be
      mft_loc_sector = (mft_cluster * spc) + partition_start_s
      IO.puts("MFT cluster #: #{mft_cluster} => MFT Location (sectors): #{mft_loc_sector}")


      IO.puts("\n\nFinding start of MFT Mirror from MBR")
      mft_mirror_cluster = hd_no_offset(partition_start + 56, 8, image) |> htoi_be
      mft_mirror_loc_sector = (mft_mirror_cluster * spc) + partition_start_s
      IO.puts("MFT Mirror cluster #: #{mft_mirror_cluster} => MFT Mirror Location (sectors): #{mft_mirror_loc_sector}")

      # Print MFT Header
      hd_char(mft_loc_sector * spb, spb, image)

      num_sys_files = 27
      mft_entry_size = 1024

      num_files = 3

      for n <- 0 .. num_files-1 do
        start_of_entry = (mft_loc_sector * spb) + ((num_sys_files + n) * mft_entry_size)
        hd_char(start_of_entry, 1024, image)
        IO.puts("\n\nFinding offset to first attr")
        # offset_first_attr = hd_no_offset(mft_loc_sector * spb + 20, 1024, image) |> htoi_be
        # IO.puts("Offset to first attr: #{offset_first_attr}")
      end




      # reserved_sectors = hd_no_offset(partition_start + 14, 2, image) |> htoi_be
      # num_fats = hd_no_offset(partition_start + 16, 1, image) |> htoi_be
      # sectors_per_fat = hd_no_offset(partition_start + 22, 2, image) |> htoi_be
      # sectors_before_partition = hd_no_offset(partition_start + 28, 4, image) |> htoi_be
      # IO.puts("\n\n")
      # IO.puts(">>>>>> PARTITION #{i}")

      # IO.puts("sect per clust #{spc}")
      # IO.puts("reserved_sectors #{reserved_sectors}")
      # IO.puts("num_fats #{num_fats}")
      # IO.puts("sectors_per_fat #{sectors_per_fat}")
      # IO.puts("sectors_before_partition #{sectors_before_partition}")


      # fat_start = partition_start + (reserved_sectors * spb)
      # IO.puts("fat start #{fat_start/spb}")
      # fat = hd_char(fat_start, sectors_per_fat * spb, image)
      # fat_clusters = hd_no_offset(fat_start, (sectors_per_fat * spb), image)
      #   |> String.split(~r/.{1,4}/, trim: true, include_captures: true)
      # IO.puts("num clust #{Enum.count(fat_clusters)}")
      # IO.puts(hd_char(fat_start, spb, image))

      # IO.puts("Root Directory")
      # root_dir_start = fat_start + (2 * sectors_per_fat * spb)
      # IO.puts("root #{root_dir_start/spb}")
      # root_dir = hd_char(root_dir_start, 32*spb, image)
      # root_dir_entries = hd_no_offset(root_dir_start, 32*spb, image)
      #   |> String.split(~r/.{1,128}/, trim: true, include_captures: true)
      #   |> Enum.reject(fn x -> x == String.duplicate("0", 128) end)

      # IO.puts(root_dir)
      # Enum.each(root_dir_entries, fn entry ->
      #   [status, _, name, ext, attr, reserved, time, date, cluster, size] = cut_string(entry, [2, 62, 16, 6, 2, 20, 4, 4, 4, 8])

      #   IO.puts("Status: #{status} => #{@file_statuses[status]}")
      #   IO.puts("Name: #{name} => #{htoa(name)}")
      #   IO.puts("Extention: #{ext} => #{htoa(ext)}")
      #   IO.puts("Attribute: #{attr} => #{@file_attributes[attr]}")
      #   IO.puts("Reserved: #{reserved}")

      #   IO.puts("Cluster: #{cluster} => #{htoi_be(cluster)}")
      #   IO.puts("Size: #{size} => #{htoi_be(size)} bytes")

      #   file_sector_count = ceil(htoi_be(size)/spb)
      #   data_area_start = partition_start_s + reserved_sectors + (num_fats * sectors_per_fat) + (32)
      #   IO.puts("data_area_start #{data_area_start}")
      #   sector_start = data_area_start + ((htoi_be(cluster) - 2) * spc)
      #   IO.puts("sectorstart #{sector_start}")
      #   IO.puts(file_sector_count)
      #   {_dd_output, _exit_code} = System.cmd("dd", ["if=#{image}", "of=out/#{String.trim(htoa(name))}.#{String.downcase(htoa(ext))}", "bs=spb", "skip=#{sector_start}","count=#{trunc(file_sector_count)}"])
      #   IO.puts("\n")
      # end)



    end)


  end
end

Extract.run()
