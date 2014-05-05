package gui.threed;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import document.threed.TDModelNode;

public class SimpleThreeDModelViewer extends Pane{
	
	private Group root;
	private Group axisGroup;
	private TDModelNode world;
	private PerspectiveCamera camera;
	private TDModelNode cameraXForm;
	private TDModelNode cameraXForm2;
	private TDModelNode cameraXForm3;
	private double cameraDistance;
	private TDModelNode moleculeGroup;
	/*
	private Timeline timeline;
	private boolean timelinePlaying = false;
	private double ONE_FRAME = 1.0 / 24.0;
	private double DELTA_MULTIPLIER = 200.0;
	private double CONTROL_MULTIPLIER = 0.1;
	private double SHIFT_MULTIPLIER = 0.1;
	private double ALT_MULTIPLIER = 0.5;
	*/
	private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    
    private SubScene subScene;
	
	public SimpleThreeDModelViewer() {
		initGui();
		buildCamera();
		buildAxes();
		buildMolecule();
		buildScene();
		initEvents();
	}

	private void initEvents() {
		setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;
                double modifierFactor = 0.3;

                if (me.isControlDown()) {
                    modifier = 0.1;
                }
                if (me.isShiftDown()) {
                    modifier = 10.0;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraXForm.ry.setAngle(cameraXForm.ry.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0);  // +
                    cameraXForm.rx.setAngle(cameraXForm.rx.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0);  // -
                } else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX * modifierFactor * modifier;
                    camera.setTranslateZ(newZ);
                } else if (me.isMiddleButtonDown()) {
                    cameraXForm2.t.setX(cameraXForm2.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
                    cameraXForm2.t.setY(cameraXForm2.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
                }
            }
        });
	}

	private void initGui() {
		root = new Group();
		axisGroup = new Group();
		world = new TDModelNode();
		camera = new PerspectiveCamera(true);
		cameraXForm = new TDModelNode();
		cameraXForm2 = new TDModelNode();
		cameraXForm3 = new TDModelNode();
		cameraDistance = 450;
		moleculeGroup = new TDModelNode();
		subScene = new SubScene(this, 250, 250, true, SceneAntialiasing.BALANCED);
		subScene.setCamera(camera);
		subScene.setRoot(root);
		subScene.setFill(Color.LIGHTBLUE);
		subScene.autosize();
		this.getChildren().add(subScene);
	}

	private void buildMolecule() {
		final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);

        // Molecule Hierarchy
        // [*] moleculeXform
        //     [*] oxygenXForm
        //         [*] oxygenSphere
        //     [*] hydrogen1SideXForm
        //         [*] hydrogen1XForm
        //             [*] hydrogen1Sphere
        //         [*] bond1Cylinder
        //     [*] hydrogen2SideXForm
        //         [*] hydrogen2XForm
        //             [*] hydrogen2Sphere
        //         [*] bond2Cylinder

        TDModelNode moleculeXForm = new TDModelNode();
        TDModelNode oxygenXForm = new TDModelNode();
        TDModelNode hydrogen1SideXForm = new TDModelNode();
        TDModelNode hydrogen1XForm = new TDModelNode();
        TDModelNode hydrogen2SideXForm = new TDModelNode();
        TDModelNode hydrogen2XForm = new TDModelNode();

        Sphere oxygenSphere = new Sphere(40.0);
        oxygenSphere.setMaterial(redMaterial);

        Sphere hydrogen1Sphere = new Sphere(30.0);
        hydrogen1Sphere.setMaterial(whiteMaterial);
        hydrogen1Sphere.setTranslateX(0.0);

        Sphere hydrogen2Sphere = new Sphere(30.0);
        hydrogen2Sphere.setMaterial(whiteMaterial);
        hydrogen2Sphere.setTranslateZ(0.0);

        Cylinder bond1Cylinder = new Cylinder(5, 100);
        bond1Cylinder.setMaterial(greyMaterial);
        bond1Cylinder.setTranslateX(50.0);
        bond1Cylinder.setRotationAxis(Rotate.Z_AXIS);
        bond1Cylinder.setRotate(90.0);

        Cylinder bond2Cylinder = new Cylinder(5, 100);
        bond2Cylinder.setMaterial(greyMaterial);
        bond2Cylinder.setTranslateX(50.0);
        bond2Cylinder.setRotationAxis(Rotate.Z_AXIS);
        bond2Cylinder.setRotate(90.0);

        moleculeXForm.getChildren().add(oxygenXForm);
        moleculeXForm.getChildren().add(hydrogen1SideXForm);
        moleculeXForm.getChildren().add(hydrogen2SideXForm);
        oxygenXForm.getChildren().add(oxygenSphere);
        hydrogen1SideXForm.getChildren().add(hydrogen1XForm);
        hydrogen2SideXForm.getChildren().add(hydrogen2XForm);
        hydrogen1XForm.getChildren().add(hydrogen1Sphere);
        hydrogen2XForm.getChildren().add(hydrogen2Sphere);
        hydrogen1SideXForm.getChildren().add(bond1Cylinder);
        hydrogen2SideXForm.getChildren().add(bond2Cylinder);

        hydrogen1XForm.setTx(100.0);
        hydrogen2XForm.setTx(100.0);
        hydrogen2SideXForm.setRotateY(104.5);

        moleculeGroup.getChildren().add(moleculeXForm);

        world.getChildren().addAll(moleculeGroup);
	}

	private void buildAxes() {
		final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
        
        final Box xAxis = new Box(240.0, 1, 1);
        final Box yAxis = new Box(1, 240.0, 1);
        final Box zAxis = new Box(1, 1, 240.0);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        world.getChildren().addAll(axisGroup);
	}

	private void buildCamera() {
		root.getChildren().add(cameraXForm);
		cameraXForm.getChildren().add(cameraXForm2);
		cameraXForm2.getChildren().add(cameraXForm3);
		cameraXForm3.getChildren().add(camera);
		cameraXForm3.setRotateZ(180);
		
		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-cameraDistance);
		cameraXForm.ry.setAngle(320);
		cameraXForm.rx.setAngle(40);
	}

	private void buildScene() {
		root.getChildren().add(world);
	}
	
}
