booter.o:
	make -C ../boot

ImgGenerator.class: ./ImgGenerator.java
	javac -encoding UTF-8 ./ImgGenerator.java

softDisk.img: booter.o ImgGenerator.class
	java ImgGenerator

clean:
	make -C ../boot clean
	rm -f ImgGenerator.class