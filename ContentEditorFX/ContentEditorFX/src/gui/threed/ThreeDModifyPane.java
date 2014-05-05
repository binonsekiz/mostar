package gui.threed;

import geometry.libgdxmath.Vector2;
import geometry.libgdxmath.Vector3;
import gui.helper.FontHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import document.threed.TDCamera;
import document.threed.TDModelNode;

public class ThreeDModifyPane extends Pane{

	private Canvas overlayCanvas;
	private GraphicsContext context;
	private int refreshCount;
	
	private SubScene scene;
	private Group root;
	private TDCamera camera;
	private Font fontSmall;
	@SuppressWarnings("unused")
	private Font fontLarge;
	
	private Vector2 mousePos;
	private Vector2 mouseOld;
	private Vector2 mouseDelta;
	private Vector3 mouseOnXZPlane;
	
	private Box mouseBox1;
	private Box mouseBox2;
	private Box xzPlane;
	
	public ThreeDModifyPane() {
		mousePos = new Vector2();
		mouseOld = new Vector2();
		mouseDelta = new Vector2();
		mouseOnXZPlane = new Vector3();
		mouseBox1 = new Box(1,1,1);
		mouseBox2 = new Box(1,1,1);
		initGui();
		initEvents();
		revalidateSize();
	}

	private void initGui() {
		overlayCanvas = new Canvas();
		context = overlayCanvas.getGraphicsContext2D();
		root = new Group();
		scene = new SubScene(this, this.getWidth(), this.getHeight(), true, SceneAntialiasing.BALANCED);
		camera = new TDCamera();
		scene.setRoot(root);
		scene.setCamera(camera);
		
		fontSmall = FontHelper.getFont("larabie", 12);
		fontLarge = FontHelper.getFont("larabie", 48);
		context.setFont(fontSmall);
				
		buildAxes();
		
		this.getChildren().addAll(scene, overlayCanvas);
	}
	
	private void initEvents() {
		this.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				revalidateSize();
			}
		});
		
		this.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				revalidateSize();
			}
		});
		
		this.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent me) {
                mousePos.x = (float) me.getX();
                mousePos.y = (float) me.getY();
                mouseOld.x = (float) me.getX();
                mouseOld.y = (float) me.getY();
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOld.x = mousePos.x;
                mouseOld.y = mousePos.y;
                mousePos.x = (float) me.getX();
                mousePos.y = (float) me.getY();
                mouseDelta.x = (mousePos.x - mouseOld.x);
                mouseDelta.y = (mousePos.y - mouseOld.y);
                TDModelNode cameraXForm = camera.getCameraXForm();
                TDModelNode cameraXForm2 = camera.getCameraXForm2();

                double modifier = 1.0;
                double modifierFactor = 0.3;

                if (me.isControlDown()) {
                    modifier = 0.1;
                }
                if (me.isShiftDown()) {
                    modifier = 10.0;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraXForm.ry.setAngle(cameraXForm.ry.getAngle() - mouseDelta.x * modifierFactor * modifier * 2.0);  // +
                    cameraXForm.rx.setAngle(cameraXForm.rx.getAngle() + mouseDelta.y * modifierFactor * modifier * 2.0);  // -
                } else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDelta.x * modifierFactor * modifier;
                    camera.setTranslateZ(newZ);
                } else if (me.isMiddleButtonDown()) {
                    cameraXForm2.t.setX(cameraXForm2.t.getX() + mouseDelta.x * modifierFactor * modifier * 0.3);  // -
                    cameraXForm2.t.setY(cameraXForm2.t.getY() + mouseDelta.y * modifierFactor * modifier * 0.3);  // -
                }
            }
        });
        this.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mouseOld.x = mousePos.x;
                mouseOld.y = mousePos.y;
                mousePos.x = (float) arg0.getX();
                mousePos.y = (float) arg0.getY();
				repaintCanvas();
				calculateMouseBox(arg0);
			}
		});
	}

	protected void calculateMouseBox(MouseEvent arg0) {
		mouseBox1.setWidth(arg0.getSceneX());
		mouseBox2.setDepth(arg0.getSceneY());
	
	}

	protected void revalidateSize() {
		overlayCanvas.setWidth(this.getWidth());
		overlayCanvas.setHeight(this.getHeight());
		scene.setWidth(this.getWidth());
		scene.setHeight(this.getHeight());
		overlayCanvas.toFront();
	}
	
	private void repaintCanvas() {
		context.clearRect(0, 0, overlayCanvas.getWidth(), overlayCanvas.getHeight());
		refreshCount ++;
		paintInfoLabels();
	}
	
	private void paintInfoLabels(){
		context.save();
		context.setLineWidth(0.5f);
		context.setStroke(Color.FORESTGREEN);
		context.strokeText("Refresh count: " + refreshCount, 10, 20);
		context.strokeText("Mouse x: " + mousePos.x + ", y: " + mousePos.y, 10, 35);
		Point3D result = localToScene(new Point3D(mousePos.x, mousePos.y, 0));
		
		context.strokeText("Scene " + result, 10, 50);
		context.restore();
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
        
        final Box xAxis = new Box(1000.0, 0.3, 0.3);
        final Box yAxis = new Box(0.3, 1000.0, 0.3);
        final Box zAxis = new Box(0.3, 0.3, 1000.0);

        Label xText = new Label("+X");
        
        xzPlane = new Box(1000,1,1000);
        xzPlane.setTranslateY(-0.5);
        xzPlane.setMaterial(greenMaterial);
        
        xzPlane.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				System.out.println("Mouse event");
				mouseOnXZPlane.x = (float) arg0.getX();
				mouseOnXZPlane.y = (float) arg0.getY();
				mouseOnXZPlane.z = (float) arg0.getZ();
			}
        });
        
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
        
        mouseBox1.setMaterial(redMaterial);
        mouseBox2.setMaterial(blueMaterial);
        
        root.getChildren().addAll(xAxis, yAxis, zAxis, xText, mouseBox1, mouseBox2, xzPlane);
	}

	
	
}
