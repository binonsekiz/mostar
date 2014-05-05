package document.threed;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.paint.PhongMaterial;

public class TDScene extends Group{

	@SuppressWarnings("unused")
	private TDModelNode root;
	@SuppressWarnings("unused")
	private ArrayList<PerspectiveCamera> cameras;
	@SuppressWarnings("unused")
	private ArrayList<PhongMaterial> materials;
	
	public TDScene() {
		cameras = new ArrayList<PerspectiveCamera>();
		materials = new ArrayList<PhongMaterial>();
	}
	
}
