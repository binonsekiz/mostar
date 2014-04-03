package document.threed;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.paint.PhongMaterial;

public class TDScene extends Group{

	private TDModelNode root;
	private ArrayList<PerspectiveCamera> cameras;
	private ArrayList<PhongMaterial> materials;
	
	public TDScene() {
		cameras = new ArrayList<PerspectiveCamera>();
		materials = new ArrayList<PhongMaterial>();
	}
	
}
