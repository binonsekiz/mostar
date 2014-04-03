package document.threed;

import javafx.scene.PerspectiveCamera;

public class TDCamera extends PerspectiveCamera{
	
	private TDModelNode cameraXForm;
	private TDModelNode cameraXForm2;
	private TDModelNode cameraXForm3;
	
	public TDCamera() {
		super(true);
		cameraXForm = new TDModelNode();
		cameraXForm2 = new TDModelNode();
		cameraXForm3 = new TDModelNode();
		buildCamera();	
	}
	
	private void buildCamera() {
		cameraXForm.getChildren().add(cameraXForm2);
		cameraXForm2.getChildren().add(cameraXForm3);
		cameraXForm3.getChildren().add(this);
		cameraXForm3.setRotateZ(180);
				
		setNearClip(0.1);
		setFarClip(10000.0);
		setTranslateZ(-500);
		cameraXForm.ry.setAngle(320);
		cameraXForm.rx.setAngle(40);
	}
	
	public TDModelNode getRoot() {
		return cameraXForm;
	}
	
	public TDModelNode getCameraXForm() {
		return cameraXForm;
	}

	public TDModelNode getCameraXForm2() {
		return cameraXForm2;
	}

	public TDModelNode getCameraXForm3() {
		return cameraXForm3;
	}
	
}
