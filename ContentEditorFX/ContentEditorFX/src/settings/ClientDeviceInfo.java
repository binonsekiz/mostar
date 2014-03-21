package settings;

public class ClientDeviceInfo {
	
	private String name;
	private String manufacturer;
	private String model;
	private double screenSize;
	private DeviceType deviceType;
	private int pixelWidth;
	private int pixelHeight;
	private double dpi;
	private double actualWidth;
	private double actualHeight;
	
	protected ClientDeviceInfo(String name, String manufacturer, String model, int pixelWidth, int pixelHeight, DeviceType deviceType) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.model = model;
		this.pixelHeight = pixelHeight;
		this.pixelWidth = pixelWidth;
		this.deviceType = deviceType;
	}
	
	protected void setActualSize(double width, double height) {
		this.actualWidth = width;
		this.actualHeight = height;
	}
	
	protected void setDpi(double dpi) {
		this.dpi = dpi;
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}

	public double getDpi() {
		return dpi;
	}

	public double getActualWidth() {
		return actualWidth;
	}

	public double getActualHeight() {
		return actualHeight;
	}

	public String getName() {
		return name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getModel() {
		return model;
	}

	public double getScreenSize() {
		return screenSize;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}
}
