public interface ImageProcessor {
	Image resizeImage(Image image, int width, int height);

	Image correlateImage(Image image, Image kernel);
}
