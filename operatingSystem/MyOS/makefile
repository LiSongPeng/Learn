ImgGenerator.class:
	make -C tools ImgGenerator.class

makeImage: ImgGenerator.class
	make -C tools softDisk.img
clean:
	make -C boot clean
	make -C tools clean
	rm -f output/softDisk.img