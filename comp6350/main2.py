import binwalk
import sys

disk = sys.argv[1] # Retreive disk image name from command line : python script.py [disk]

print(disk)



for module in binwalk.scan(sys.argv[1], signature=True, quiet=True, extract=True):
    for result in module.results:
        print(result.)
        if result.file.path in module.extractor.output:
            # These are files that binwalk carved out of the original firmware image, a la dd
            if result.offset in module.extractor.output[result.file.path].carved:
                print("Carved data from offset 0x%X to %s" % (result.offset, module.extractor.output[result.file.path].carved[result.offset]))
            # These are files/directories created by extraction utilities (gunzip, tar, unsquashfs, etc)
            if result.offset in module.extractor.output[result.file.path].extracted:
                print("Extracted %d files from offset 0x%X to '%s' using '%s'" % (len(module.extractor.output[result.file.path].extracted[result.offset].files),
                                                                                  result.offset,
                                                                                  module.extractor.output[result.file.path].extracted[result.offset].files[0],
                                                                                  module.extractor.output[result.file.path].extracted[result.offset].command))
                
                
                
