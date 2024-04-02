import subprocess
import math
import hashlib

disk = "Project2.dd"

FOOTER_BASED_SIGNATURES = { # header signature, footer signature
    "MPG": [r"\x00\x00\x01\xB3\x14", r"\x00\x00\x01\xB7"],
    "PDF": [r"\x25\x50\x44\x46", r"\x25\x25\x45\x4F\x46"],
    "DOCX": [r"\x50\x4B\x03\x04\x14\x00\x06\x00", r"\x50\x4B\x05\x06"],
    "PNG": [r"\x89\x50\x4E\x47\x0D\x0A\x1A\x0A", r"\x49\x45\x4E\x44\xAE\x42\x60\x82"],
    "JPG": [r"\xFF\xD8\xFF\xE0", r"\xFF\xD9"],
    "GIF": [r"\x47\x49\x46\x38\x39\x61", r"\x00\x00\x3B"]
}

HEADER_BASED_SIGNATURES = { # header signature, skip #, take #
    "AVI": [r"\x52\x49\x46\x46", 4, 4],
    "BMP": [r"\x42\x4D\x76\x30\x01", 2, 4] 
}

def extract_files():
    file_num = 0

    for file_type, signature in HEADER_BASED_SIGNATURES.items():
        print(file_type)
        header_sig = signature[0]
        skip = signature[1]
        take = signature[2]

        headers, error = execute_command(f"""binwalk -R "{header_sig}" {disk}""")
        header_offsets = get_binwalk_offsets(headers, header_sig)

        for start_offset in header_offsets:

            output, error = execute_command(f"""hexdump {disk} -C  -s {start_offset + skip} -n {take}""") # get file size
            little_endian = output.splitlines()[0].split(" ")[2:(2 + take)]
            big_endian = "".join((reversed(little_endian))) # format size
            file_size = int(big_endian, 16)
            end_offset = start_offset + file_size

            file_num += 1
            extract_and_hash(file_num, file_type, file_size, start_offset, end_offset)
    
    for file_type, signature in FOOTER_BASED_SIGNATURES.items():
        print(file_type)
        header_sig = signature[0]
        footer_sig = signature[1]

        headers, error = execute_command(f"""binwalk -R "{header_sig}" {disk}""")
        header_offsets = get_binwalk_offsets(headers, header_sig)

        footers, error = execute_command(f"""binwalk -R "{footer_sig}" {disk}""")
        footer_offsets = get_binwalk_offsets(footers, footer_sig)

        file_count = min(len(header_offsets), len(footer_offsets))

        # proper amount of headers, need to find corresponding footers
        if len(header_offsets) == file_count:
            for start_offset in header_offsets:

                # find next header, infinity if it does not exist
                header_index = header_offsets.index(start_offset)
                next_header = (math.inf if (header_index + 1 >= len(header_offsets)) else header_offsets[header_index + 1])

                # find the footer that is closest to start_offset and comes before the next header
                end_offset = -1
                for footer in footer_offsets:
                    if footer > end_offset and footer < next_header and footer > end_offset:
                        end_offset = footer
                
                file_size = end_offset - start_offset
                            
                file_num += 1
                extract_and_hash(file_num, file_type, file_size, start_offset, end_offset)
        else: # proper amount of footers, need to find corresponding headers
            for end_offset in footer_offsets:

                # find previous footer, -1 if it does not exist
                footer_index = footer_offsets.index(end_offset)
                prev_footer = (-1 if (footer_index - 1 < 0) else footer_offsets[footer_index - 1])

                # find the header that is closest to end_offset and comes after the previous footer 
                start_offset = math.inf
                for header in header_offsets:
                    if header < end_offset and header > prev_footer and header < start_offset:
                        start_offset = header
                
                
                file_size = end_offset - start_offset
                            
                file_num += 1
                extract_and_hash(file_num, file_type, file_size, start_offset, end_offset)
    
    print(f"{file_num} files found :D")

def get_binwalk_offsets(binwalk_output, signature):
    offsets = []
    for line in binwalk_output.splitlines():
        if signature in line:
            offsets.append(int(line.split()[0]))
    return offsets

def extract_and_hash(file_num, file_type, file_size, start_offset, end_offset):
    filename = f"file{file_num}.{file_type.lower()}"
    print(f"Extracting {filename} ... | Start offset: {start_offset} | End offset: {end_offset}")
    output, error = execute_command(f"""dd if={disk} of={f"file{file_num}.{file_type.lower()}"} bs=1 skip={start_offset} count={file_size}""")

    with open(filename, 'rb', buffering=0) as f:
        sha = hashlib.file_digest(f, 'sha256').hexdigest()
    print(f"\tSHA-256 of recovered {filename}: {sha}\n")

def execute_command(command):
    process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
    output, error = process.communicate()
    return output.decode(), error.decode() 

extract_files()