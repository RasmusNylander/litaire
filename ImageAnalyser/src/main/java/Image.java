public interface Image {
	int getWidth();

	int getHeight();

	int getPixel(int x, int y) throws IndexOutOfBoundsException;

	void setPixel(int x, int y, int value) throws IndexOutOfBoundsException, InvalidPixelValueException;
}
