# References:
# https://stackoverflow.com/questions/89228/how-do-i-execute-a-program-or-call-a-system-command
# https://stackoverflow.com/questions/6787233/python-how-to-read-bytes-from-file-and-save-it
# https://stackoverflow.com/questions/36490354/working-with-bytes-in-python-using-sha-256
# https://stackoverflow.com/questions/27697218/python-regex-search-for-hexadecimal-bytes
# https://www.garykessler.net/library/file_sigs.html

import subprocess
import hashlib
import sys
import re
import os

# hd: possible header signatures, tl: possible trailing signatures
signatures = {
    "pdf": {
        "hd": [b"\x25\x50\x44\x46"],
        "tl": [
            b"\x0a\x25\x25\x45\x4f\x46\x0a",
            b"\x0d\x0a\x25\x25\x45\x4f\x46\x0d\x0a",
            b"\x0d\x25\x25\x45\x4f\x46\x0d",
        ],
    },
    "gif": {
        "hd": [b"\x47\x49\x46\x38\x37\x61", b"\x47\x49\x46\x38\x39\x61"],
        "tl": [b"\x00\x3b\x00\x00"],
    },
    "jpg": {
        "hd": [
            b"\xff\xd8\xff\xe0",
        ],
        "tl": [b"\xff\xd9"],
    },
    "avi": {"hd": [b"\x52\x49\x46\x46"]},  # next 4 bytes are file size
    "png": {
        "hd": [b"\x89\x50\x4e\x47\x0d\x0a\x1a\x0a"],
        "tl": [b"\x49\x45\x4e\x44\xae\x42\x60\x82"],
    },
}


def sha256(byte_list):
    h = hashlib.sha256(byte_list)
    return h.hexdigest()


def sha1(byte_list):
    h = hashlib.sha1(byte_list)
    return h.hexdigest()


def recover_files(image_name, out_dir):
    # Ensure output directory exists
    if not os.path.exists(out_dir):
        os.makedirs(out_dir)

    # Open image bytes
    with open(image_name, "rb") as f:
        image = f.read()

    print(f"Image {image_name} loaded with {len(image)} bytes")

    files = []  # [[type (name), start offset, end offset, sha256, sha1]]
    for file_type in signatures:
        signature = signatures[file_type]
        hds = signature["hd"]

        # Find all occurences of each possible header signature
        for hd in hds:
            hd_regex = re.compile(hd)
            for hd_match_obj in hd_regex.finditer(image):
                hd_offset_d = hd_match_obj.start()  # Decimal offset for an occurence

                if file_type.lower() == "avi":
                    # Get 4 bytes after header for file size
                    file_size_b = image[
                        hd_offset_d + len(hd) : hd_offset_d + len(hd) + 4
                    ]
                    file_size_d = int.from_bytes(file_size_b, "little")
                    end_offset = hd_offset_d + len(hd) + 4 + file_size_d
                    file_bytes = image[hd_offset_d:end_offset]

                    files.append(
                        [
                            f"File{len(files)}.{file_type}",
                            hd_offset_d,
                            end_offset,
                            sha256(file_bytes),
                            sha1(file_bytes),
                        ]
                    )

                else:
                    # Find all occurences of each possible trailer signature
                    tls = signature["tl"]
                    for tl in tls:
                        tl_regex = re.compile(tl)
                        tl_regex_matches = list(tl_regex.finditer(image))

                        # PDF: There may be multiple end-of-file marks within the file. When carving, be sure to get the last one.
                        if file_type.lower() == "pdf" and len(tl_regex_matches) > 0:
                            tl_regex_matches = [
                                max(
                                    tl_regex_matches,
                                    key=lambda match: match.start(),
                                    default=None,
                                )
                            ]

                        # JPG: There may be multiple end-of-file marks within the file. When carving, be sure to get the first one.
                        if file_type.lower() == "jpg" and len(tl_regex_matches) > 0:
                            tl_regex_matches = [
                                min(
                                    tl_regex_matches,
                                    key=lambda match: match.start(),
                                    default=None,
                                )
                            ]

                        for tl_match_obj in tl_regex_matches:
                            # Decimal offset for trailer occurence
                            tl_offset_d = tl_match_obj.start()
                            # Only want to consider trailers after the header
                            if tl_offset_d > hd_offset_d:
                                if file_type.lower() == "gif":
                                    tl_offset_d -= 2

                                # The "end" must include the trailer bytes
                                tl_offset_d += len(tl)
                                file_bytes = image[hd_offset_d:tl_offset_d]

                                files.append(
                                    [
                                        f"File{len(files)}.{file_type}",
                                        hd_offset_d,
                                        tl_offset_d,
                                        sha256(file_bytes),
                                        sha1(file_bytes),
                                    ]
                                )

    print(f"Extracting {len(files)} files to {out_dir}...")
    for file in files:
        # Run dd to extract the file, ignore all output
        subprocess.run(
            f"dd if={image_name} of={out_dir}/{file[0]} bs=1 skip={file[1]} count={file[2]-file[1]}",
            shell=True,
            stdout=subprocess.DEVNULL,
            stderr=subprocess.DEVNULL,
        )
        # Pretty-print file details
        print(f"\n{file[0]}")
        print(
            f"\tStart Offset: {hex(file[1])} ({file[1]}) | End Offset: {hex(file[2])} ({file[2]})"
        )
        print(f"\tFile Size: {file[2]-file[1]} bytes")
        print(f"\tsha256: {file[3]}")
        print(f"\tsha1: {file[4]}")


if __name__ == "__main__":
    image_name, out_dir = sys.argv[1:]
    recover_files(image_name, out_dir)
