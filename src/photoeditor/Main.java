package photoeditor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.animation.AnimationTimer;
import javax.imageio.ImageIO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import photoeditor.Properties.ThemeProperty;

public class Main extends Application {

    public Stage primaryStage;
    
    private static double SWIDTH = 1280;
    private static double SHEIGHT = 720;

    ColorAdjust effect = new ColorAdjust(0, 0, 0, 0);

    final Canvas canvas = new Canvas();
    final Canvas underCanvas = new Canvas();
    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    GraphicsContext graphicsContextUnder = underCanvas.getGraphicsContext2D();

    private String filename = "";
    StackPane layerContainer = new StackPane(); //Creates a container to hold all Layer Objects.
    ScrollPane scrollPane = new ScrollPane();
   
    
    //for click and drag
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    BorderPane window = new BorderPane();

    public Scene scene = new Scene(window, SWIDTH, SHEIGHT);
    public SubScene scene2 = new SubScene(layerContainer, 512, 512);
    //public Scene scene2 = new Scene(root, 1920, 1080);

    Group root = new Group();
    //Scene scene = new Scene(root, 800, 800);

    /** variables for Undo/Redo*/
    int iteration = 0;
    int undoCount = 0;
    boolean firstRedo;
    int redoCount = 0;
    WritableImage snap;

    private ToggleButton file;
    private ToggleButton edit;
    private ToggleButton effects;
    private ToggleButton toolsel;
    
    private Button settingsButton;

    private Button newButton;
    private Button openButton;
    private Button saveButton;
    private Button saveasButton;
    private Button undoButton;
    private Button redoButton;
    private Button colourizeButton;
    private Button contrastButton;
    private Button brightnessButton;
    private Button monochromeButton;
    private Button invertButton;
    private Button ditherButton;
    private Button resizeButton;
    private Button pixeliseButton;
    private Button curvesButton;
    private Button contentAwareScaleButton;
    private TextField seamsToRemoveButton;
    private ToggleButton dropper;

    private ColorPicker colorpicker;
    private TextField brushSize;
    private TextField rotateDegree;
    private ToggleButton eraser;
    private ToggleButton drag;
    private ToggleButton ZoomIn;
    private ToggleButton ZoomOut;
    private ToggleButton paintbrush;
    private ToggleButton rotate;
    private ToggleButton select;
    private Button rotateC;
    private Button rotateA;
    private Button resetButton;
    private Button resetZoom;
    private Button savedZoom;
    final Label heightLabel = new Label();
    final Label widthLabel = new Label();
    double imageWidth; 
    double imageHeight;
    double scaleWidth;
    double scaleHeight;
    double pixelScale = 1;
    
    //File fileRotC = new File("/resources/rotClockwise.png");
    //File fileRotA = new File("/resources/rotAntiClockwise.png");
    //File fileErasr = new File("/resources/eraserIcon.png");
    //File fileNew = new File("/resources/NewFileIconsml.png");
    //File fileOpen = new File("/resources/openIconsml.png");
    //File fileSave = new File("/resources/SaveasIconsml.png");
    //File fileSaveas = new File("/resources/SaveasIconsml.png");
    //File fileDrag = new File("/resources/DragIcon.png");
    //File fileZoomIn = new File("/resources/ZoomIn.png");
    //File fileZoomOut = new File("/resources/ZoomOut.png");
    //File fileUndo = new File("/resources/undoIconsml.png");
    //File fileBrush = new File("/resources/paintBrushIconsml.png");
    //File filePicker = new File("/resources/ColourPickerSml.png");
    //File fileReset = new File("/resources/resetIconSml.png");
    //Image iconImgrotC = new Image(fileRotC.toURI().toString());
    //Image iconImgrotA = new Image(fileRotA.toURI().toString());
    //Image iconImgErasr = new Image(fileErasr.toURI().toString());
    //Image iconImgDrag = new Image(fileDrag.toURI().toString());
    //Image iconImgZoomIn = new Image(fileZoomIn.toURI().toString());
    //Image iconImgZoomOut = new Image(fileZoomOut.toURI().toString());
    //Image iconImgNew = new Image(fileNew.toURI().toString());
    //Image iconImgOpen = new Image(fileOpen.toURI().toString());
    //Image iconImgSave = new Image(fileSave.toURI().toString());
    //Image iconImgSaveas = new Image(fileSaveas.toURI().toString());
    //Image iconImgUndo = new Image(fileUndo.toURI().toString());
    //Image iconImgBrush = new Image(fileBrush.toURI().toString());
    //Image iconImgPicker = new Image(filePicker.toURI().toString());
    //Image iconImgReset = new Image(fileReset.toURI().toString());
    final ToggleGroup group = new ToggleGroup();
    final ToggleGroup grouptwo = new ToggleGroup();
    VBox topContainer = new VBox();  //Creates a container to hold all Menu Objects.
    
    public boolean fileActive = false;
    public boolean editActive = false;
    public boolean effectsActive = false;
    public boolean toolboxActive = false;

    ToolBar menu = new ToolBar();
    ToolBar fileBar = new ToolBar();
    ToolBar editBar = new ToolBar();
    ToolBar effectsBar = new ToolBar();
    ToolBar toolbar = new ToolBar();
    double zoomCounter = 2;
    private ZoomHandler zoomOperator = new ZoomHandler();
    SelectHandler selectHandler = new SelectHandler();
      
    Point2D mouseLocation = new Point2D( 0, 0);
    //Point2D prevMouseLocation = new Point2D( 0, 0);
    
    /**Point2D**/ //double mouseLocationX;
    //double mouseLocationY;
    Point2D prev;
    
    double prevX;
    double prevY;
    
    AnimationTimer loop;
    boolean mousePressed = false;
    double xOld;
    double yOld;
    
    double brushMaxSize = 30;
    public Image brush = createBrush(brushMaxSize, Color.BLACK);
    public double brushWidthHalf = brush.getWidth() / 2.0;
    public double brushHeightHalf = brush.getHeight() / 2.0;
    double pressure = 0;
    double pressureDelay = 0.04;
    //Image[] brushVariations = new Image[256];

    @Override
    public void start(Stage primaryStage) throws IOException {

        ThemeProperty themeProperty = new ThemeProperty();
        //themeProperty.SaveProperty("0");
        String theme = themeProperty.LoadProperty();
        System.out.println("theme is: " + theme + ".css");
        scene.getStylesheets().add(getClass().getResource(theme + "Theme.css").toExternalForm());
        
        scrollPane.setOnZoom(null);
        //scene.setCamera(camera);
        
        /**
         * *new stuff for 09/11/17**
         */

        //graphicsContext.setFill(Color.rgb(1, 1, 1, 0.1));
        //graphicsContext.fillRect(0,0,512,512);
        //graphicsContext.drawImage(image, 0, 0, imageView.getImage().getWidth(), imageView.getImage().getHeight());
        //canvas.getGraphicsContext2D();

        final Pane leftSpacer = new Pane();
        HBox.setHgrow(
                leftSpacer,
                Priority.ALWAYS
        );

        final Pane rightSpacer = new Pane();
        HBox.setHgrow(
                rightSpacer,
                Priority.ALWAYS
        );
        

        /**
         * **************************************************************
         *************************Menu Bar Instance**********************
         * **************************************************************
         */

        /*MenuBar menu = new MenuBar(); //Setup for the menu bar at the top of the editor
		Menu fileMenu = new Menu("File..."); //File menu
		MenuItem newItem = new MenuItem("New", null);
		MenuItem loadItem = new MenuItem("Open...", null);
		MenuItem saveItem = new MenuItem("Save", null);
		MenuItem saveAsItem = new MenuItem("Save As", null);
		MenuItem exitItem = new MenuItem("Exit", null);

		fileMenu.getItems().addAll(newItem, loadItem, saveItem, saveAsItem,
				exitItem);

		Menu editMenu = new Menu("Edit...");
		MenuItem undoItem = new MenuItem("---", null);
		MenuItem redoItem = new MenuItem("---", null);
		MenuItem cutItem = new MenuItem("---", null);
		MenuItem copyItem = new MenuItem("---", null);
		MenuItem pasteItem = new MenuItem("---", null);
		MenuItem deleteItem = new MenuItem("---", null);
		//MenuItem rotateCItem = new MenuItem("Rotate Clockwise", null);
		//MenuItem rotateAItem = new MenuItem("Rotate Anticlockwise", null);

		editMenu.getItems().addAll(undoItem, redoItem, cutItem, copyItem,
				pasteItem, deleteItem//, rotateCItem, rotateAItem
                );

		Menu effectsMenu = new Menu("Effects...");
                MenuItem BoxBlurItem = new MenuItem("Blur", null);
                MenuItem MotionBlurItem = new MenuItem("Motion Blur", null);
                MenuItem GaussianBlurItem = new MenuItem("Gaussian Blur", null);
		MenuItem colourizeItem = new MenuItem("Colourize", null);
		MenuItem contrastItem = new MenuItem("Contrast", null);
		MenuItem brightnessItem = new MenuItem("Brightness", null);
		MenuItem monochromeItem = new MenuItem("Monochrome", null);

		effectsMenu.getItems().addAll(colourizeItem, contrastItem, brightnessItem,
				monochromeItem);*/
        // Main Menu buttons:
        
        // Please make buttons for:
        // - Dropper (already done, needs image and stuff)
        // - Reset rotation (button that calls resetRotate())
        file = new ToggleButton("File");
        edit = new ToggleButton("Edit");
        effects = new ToggleButton("Effects");
        toolsel = new ToggleButton("Toolbar");
        
        settingsButton = new Button("Settings");

        // Ribbon Buttones
        newButton = new Button("New");
        openButton = new Button("Open");
        saveButton = new Button("Save");
        saveasButton = new Button("Save As");

        undoButton = new Button("Undo");
        redoButton = new Button("Redo");

        colourizeButton = new Button("Colourize");
        colourizeButton.setTooltip(
           new Tooltip("Shift the colours in the image(change hue)")
        );
        contrastButton = new Button("Contrast");
        contrastButton.setTooltip(
           new Tooltip("Change the contrast")
        );
        brightnessButton = new Button("Brightness");
        brightnessButton.setTooltip(
           new Tooltip("Change the brightness of the image")
        );
        monochromeButton = new Button("Saturation");
        monochromeButton.setTooltip(
           new Tooltip("Convert the image to black and white")
        );
        invertButton = new Button("Invert Colours");
        invertButton.setTooltip(
           new Tooltip("Invert colours")
        );
        ditherButton = new Button("Dither Image");
        ditherButton.setTooltip(
           new Tooltip("Dither the image \n (reduces the number of colours)")
        );
        pixeliseButton = new Button("Pixelise Image");
        pixeliseButton.setTooltip(
           new Tooltip("Reduces the resolution of the image \n without altering hte size")
        );
        resizeButton = new Button("Resize Image");
        resizeButton.setTooltip(
           new Tooltip("resize the image \n by stretching it")
        );
        curvesButton = new Button("Curves");
        curvesButton.setTooltip(
           new Tooltip("Advanced tool for editing the tonal range \n of the image")
        );
        
        dropper = new ToggleButton("Dropper");
        dropper.setTooltip(
           new Tooltip("Pick a colour from \n the image")
        );
        paintbrush = new ToggleButton("Paint Brush");
        paintbrush.setTooltip(
           new Tooltip("Paintbrush")
        );
        colorpicker = new ColorPicker(Color.BLACK);
        colorpicker.setTooltip(
           new Tooltip("Colour Picker")
        );
        brushSize = new TextField("8");
        brushSize.setPromptText("Brush Size");
        brushSize.setPrefWidth(50);
        brushSize.setTooltip(
           new Tooltip("Brush size")
        );
        rotateDegree = new TextField();
        rotateDegree.setPromptText("Rotate Degrees");
        rotateDegree.setPrefWidth(120);
        rotateDegree.setTooltip(
           new Tooltip("Rotate the image by \n the specified number of degrees")
        );
        eraser = new ToggleButton("Eraser");
        eraser.setTooltip(
           new Tooltip("Eraser \n Uses left mouse button")
        );
        drag = new ToggleButton("Drag");
        drag.setTooltip(
           new Tooltip("Drag the image with \n the left mouse button")
        );
        ZoomIn = new ToggleButton("Zoom+");
        ZoomIn.setTooltip(
           new Tooltip("Zoom in")
        );
        ZoomOut = new ToggleButton("Zoom-");
        ZoomOut.setTooltip(
           new Tooltip("Zoom out")
        );
        rotateC = new Button("Rotate Clockwise");
        rotateC.setTooltip(
           new Tooltip("Rotate Clockwise")
        );
        rotateA = new Button("Rotate Anti-Clockwise");
        rotateA.setTooltip(
           new Tooltip("Rotate Anti-Clockwise")
        );
        rotate = new ToggleButton("Rotate");
        rotate.setTooltip(
           new Tooltip("Rotate the image by \n dragging the mouse")
        );
        resetButton = new Button("Reset Rotation");
        resetButton.setTooltip(
           new Tooltip("Reset image rotation")
        );
        select = new ToggleButton("Select");
        select.setTooltip(
           new Tooltip("Select a square area of the image")
        );
        contentAwareScaleButton = new Button("Content Aware scale");
        contentAwareScaleButton.setTooltip(
           new Tooltip("Scale the image without \n removing high detail content")
        );
        
        seamsToRemoveButton = new TextField("10");
        resetZoom = new Button("Reset Zoom");
        savedZoom = new Button("Saved Zoom");

        paintbrush.setToggleGroup(group);
        eraser.setToggleGroup(group);
        drag.setToggleGroup(group);
        ZoomIn.setToggleGroup(group);
        ZoomOut.setToggleGroup(group);
        dropper.setToggleGroup(group);
        rotate.setToggleGroup(group);
        select.setToggleGroup(group);

        file.setToggleGroup(grouptwo);
        edit.setToggleGroup(grouptwo);
        effects.setToggleGroup(grouptwo);
        toolsel.setToggleGroup(grouptwo);

        URL rotClockwiseURL = getClass().getResource("/resources/rotClockwise.png");
        Image iconImgrotC = new Image(rotClockwiseURL.openStream());
        URL rotAntiClockwise = getClass().getResource("/resources/rotAntiClockwise.png");
        Image iconImgrotA = new Image(rotAntiClockwise.openStream());
        URL eraserURL = getClass().getResource("/resources/eraserIcon.png");
        Image iconImgEraser = new Image(eraserURL.openStream());
        URL brushURL = getClass().getResource("/resources/paintBrushIcon.png");
        Image iconImgBrush = new Image(brushURL.openStream());
        URL dragURL = getClass().getResource("/resources/DragIcon.png");
        Image iconImgDrag = new Image(dragURL.openStream());
        URL zoomInURL = getClass().getResource("/resources/ZoomIn.png");
        Image iconImgZoomIn = new Image(zoomInURL.openStream());
        URL zoomOutURL = getClass().getResource("/resources/ZoomOut.png");
        Image iconImgZoomOut = new Image(zoomOutURL.openStream());
        URL imgNewURL = getClass().getResource("/resources/NewFileIcon.png");
        Image iconImgNew = new Image(imgNewURL.openStream());
        URL openURL = getClass().getResource("/resources/openIcon.png");
        Image iconImgOpen = new Image(openURL.openStream());
        URL saveURL = getClass().getResource("/resources/saveIcon.png");
        Image iconImgSave = new Image(saveURL.openStream());
        URL saveasURL = getClass().getResource("/resources/SaveasIcon.png");
        Image iconImgSaveas = new Image(saveasURL.openStream());
        URL undoURL = getClass().getResource("/resources/undoIcon.png");
        Image iconImgUndo = new Image(undoURL.openStream());
        URL redoURL = getClass().getResource("/resources/redoIcon.png");
        Image iconImgRedo = new Image(redoURL.openStream());
        URL pickerURL = getClass().getResource("/resources/ColourPicker.png");
        Image iconImgPicker = new Image(pickerURL.openStream());
        URL resetURL = getClass().getResource("/resources/resetIcon.png");
        Image iconImgReset = new Image(resetURL.openStream());
        URL settingsURL = getClass().getResource("/resources/settingsIcon.png");
        Image iconImgSettings = new Image(settingsURL.openStream());
        URL selectURL = getClass().getResource("/resources/selectIcon.png");
        Image iconImgSelect = new Image(selectURL.openStream());
         
        rotateC.setGraphic(new ImageView(iconImgrotC));
        rotateA.setGraphic(new ImageView(iconImgrotA));
        eraser.setGraphic(new ImageView(iconImgEraser));
        paintbrush.setGraphic(new ImageView(iconImgBrush));
        drag.setGraphic(new ImageView(iconImgDrag));
        ZoomIn.setGraphic(new ImageView(iconImgZoomIn));
        ZoomOut.setGraphic(new ImageView(iconImgZoomOut));
        newButton.setGraphic(new ImageView(iconImgNew));
        openButton.setGraphic(new ImageView(iconImgOpen));
        saveButton.setGraphic(new ImageView(iconImgSave));
        saveasButton.setGraphic(new ImageView(iconImgSaveas));
        undoButton.setGraphic(new ImageView(iconImgUndo));
        redoButton.setGraphic(new ImageView(iconImgRedo));
        dropper.setGraphic(new ImageView(iconImgPicker));
        resetButton.setGraphic(new ImageView(iconImgReset));
        settingsButton.setGraphic(new ImageView(iconImgSettings));
        select.setGraphic(new ImageView(iconImgSelect));

        menu.getItems().addAll(
                file,
                edit,
                effects,
                toolsel
        );
        menu.setPrefWidth(1080);

        toolbar.getItems().addAll(
                paintbrush,
                colorpicker,
                brushSize,
                eraser,
                dropper,
                //leftSpacer,
                rotateC,
                rotateA,
                rotateDegree,
                rotate,
                resetButton,
                drag,
                //rightSpacer,
                ZoomIn,
                ZoomOut,
                //resetZoom,
                //savedZoom,
                select
                //heightLabel,
                //widthLabel
        );
        toolbar.setPrefWidth(1080);

        fileBar.getItems().addAll(
                newButton,
                openButton,
                saveButton,
                saveasButton,
                settingsButton
        );
        fileBar.setPrefWidth(1080);

        editBar.getItems().addAll(
                undoButton,
                redoButton
        );
        editBar.setPrefWidth(1080);

        effectsBar.getItems().addAll(
                colourizeButton,
                contrastButton,
                brightnessButton,
                monochromeButton,
                invertButton,
                ditherButton,
                resizeButton,
                pixeliseButton,
                curvesButton,
                contentAwareScaleButton,
                seamsToRemoveButton
        );
        effectsBar.setPrefWidth(1080);
        
        layerContainer.getChildren().add(canvas);
        layerContainer.getChildren().add(underCanvas);
        canvas.toFront();
        //canvas.addGrid();
        underCanvas.toBack();
        //window.setCenter(layerContainer); 
        scene2.setFill(Color.BLACK);
        
        scrollPane.setPannable(true);
        final Group scrollContent = new Group(layerContainer);
        scrollPane.setContent(scrollContent);
        
        window.setCenter(scrollPane);
        
        //startAnimation();
        
        topContainer.getChildren().add(menu);
        //topContainer.getChildren().add(toolbar);
        //topContainer.getChildren().add(fileBar);
        //topContainer.getChildren().add(editBar);
        //topContainer.getChildren().add(effectsBar);
        toolbar.setVisible(false);
        fileBar.setVisible(false);
        editBar.setVisible(false);
        effectsBar.setVisible(false);
        //menu.getMenus().addAll(fileMenu, editMenu, effectsMenu);
        window.setTop(topContainer);
        primaryStage.setScene(scene);
        primaryStage.show();

        /**
         * ***********************************************************
         *************************Event Handlers**********************
         * ***********************************************************
         */
        file.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                fileRibbonHandler();
            }
        });

        edit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                editRibbonHandler();
            }
        });

        effects.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                effectsRibbonHandler();
            }
        });

        toolsel.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                toolbarRibbonHandler();
            }
        });

        newButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                newCanvas();
            }
        });

        openButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //FileHandler filehandler = new FileHandler();
                //filehandler.load();
                load();
            }
        });

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //FileHandler filehandler = new FileHandler();
                //filehandler.save();
                save();
            }
        });

        saveasButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //FileHandler filehandler = new FileHandler();
                //filehandler.saveAs();
                saveAs();
            }
        });
        
        settingsButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //FileHandler filehandler = new FileHandler();
                //filehandler.saveAs();
                settings();
            }
        });
        /*
		exitItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
                            exitProgram();
			}
		});*/

        undoButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                undo();
            }
        });
        
        redoButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                redo();
            }
        });
        
        rotateC.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                rotateClockwise();
            }
        });
        
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                resetRotation();
            }
        });

        rotateA.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                rotateAntiClockwise();
            }
        });
        
        resetZoom.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                defaultZoom(t);
            }
        });
        
        savedZoom.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                savedMZoom(t);
            }
        });

        contrastButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setContrast();
            }
        });

        brightnessButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setBrightness();
            }
        });

        monochromeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setMonochrome();
            }
        });

        colourizeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setColourize();
            }
        });
        
         pixeliseButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {  
                setPixelise();
                
            }
        });

        invertButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                InvertEffect invertEffect = new InvertEffect();
                WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
                SnapshotParameters spa = new SnapshotParameters();
                spa.setTransform(Transform.scale(pixelScale, pixelScale));
                Image image = canvas.snapshot(spa, writableImage);

             if(selectHandler.getSelectActive() == false){
                image = invertEffect.main(image);
                resetImage();
                //graphicsContext.clearRect(0, 0, image.getWidth(), image.getHeight());
                graphicsContext.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());  
             }else{
                 Image newImage = selectHandler.getSelection();
                 newImage = invertEffect.main(newImage);
                 
                 resetImage();
                 graphicsContext.drawImage(selectHandler.getOldImage(), 0, 0);
                 graphicsContext.drawImage(newImage, selectHandler.getStartX(), selectHandler.getStartY());
                 selectHandler.setOldImage(canvas.snapshot(spa, writableImage));
                 selectHandler.setSelectActive(false);
             }
            }
        });
        
        ditherButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
                DitherWindow ditherWindow = new DitherWindow();
                ditherWindow.slider.setValue(1);
                ditherWindow.label.setText(String.valueOf(1));
                ditherWindow.showStage();
                
                ditherWindow.slider.valueProperty().addListener(new ChangeListener() {
                public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                ditherWindow.label.textProperty().setValue(
                String.valueOf((int) ditherWindow.slider.getValue()));
                }
                });
                
                ditherWindow.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent we) {
                   ditherWindow.stage.close();
                }
                });
                ditherWindow.button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    DitherEffect ditherEffect = new DitherEffect();
                    
                    if(ditherWindow.checkBox.isSelected() == true){
                        effect = new ColorAdjust(effect.getHue(), (-1), effect.getBrightness(), effect.getBrightness());								//
                        canvas.setEffect(effect);
                    }
                     
                    WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
                    SnapshotParameters spa = new SnapshotParameters();
                    spa.setTransform(Transform.scale(pixelScale, pixelScale));
                    Image image = canvas.snapshot(spa, writableImage);
                
                    if(selectHandler.getSelectActive() == false){   
                        image = ditherEffect.dither(image, (int)ditherWindow.slider.getValue() - 1);
                        resetImage();
                        graphicsContext.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
                    }else{
                        Image newImage = selectHandler.getSelection();
                        newImage = ditherEffect.dither(newImage, (int)ditherWindow.slider.getValue() - 1);
                    
                        resetImage();
                        graphicsContext.drawImage(selectHandler.getOldImage(), 0, 0);
                        graphicsContext.drawImage(newImage, selectHandler.getStartX(), selectHandler.getStartY());
                        selectHandler.setOldImage(canvas.snapshot(spa, writableImage));
                        selectHandler.setSelectActive(false);
                    }
                    ditherWindow.stage.close();
                }
                });

            }
        });

        contentAwareScaleButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                snap = makeSnapshot();   
                autosave(snap, "notRedo");
                
                SeamCarveEffect seamCarveEffect = new SeamCarveEffect();

                SnapshotParameters spa = new SnapshotParameters();
                spa.setTransform(Transform.scale(pixelScale, pixelScale));
                //return canvas.snapshot(spa, writableImage);
                Image image = canvas.snapshot(spa,null);

                int seamsToRemove = Integer.parseInt(seamsToRemoveButton.getText());

                for(int i = 0; i < seamsToRemove; i++) {
                    System.out.println(i);
                    image = seamCarveEffect.main(image);
                }
                    canvas.setWidth(canvas.getWidth() - seamsToRemove);
                    resetImage();


                    graphicsContext.clearRect(0, 0, image.getWidth(), image.getHeight());
                    //canvas.setWidth(image.getWidth());
                    //canvas.setHeight(image.getHeight());

                    graphicsContext.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
            }
        });
        
         curvesButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //CurvesEffect curvesEffect = new CurvesEffect();
                WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
                SnapshotParameters spa = new SnapshotParameters();
                spa.setTransform(Transform.scale(pixelScale, pixelScale));
                //return canvas.snapshot(spa, writableImage);
                Image image = canvas.snapshot(spa,writableImage);
                
                
                CurvesWindow curvesWindow = new CurvesWindow();
                curvesWindow.label.setText("yo");
                curvesWindow.showStage(image);
 
                resetImage();
                    //resetImage();
                    //graphicsContext.clearRect(0, 0, image.getWidth(), image.getHeight());
                    graphicsContext.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
                    
                    curvesWindow.button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        try {
                            WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
                            SnapshotParameters spa = new SnapshotParameters();
                            spa.setTransform(Transform.scale(pixelScale, pixelScale));
                            Image image = canvas.snapshot(spa,null);

                            //Image image = canvas.snapshot(new SnapshotParameters(), null);


                            image = curvesWindow.applyCurve();
                            resetImage();

                            //canvas.setWidth(Integer.parseInt(resizeWindow.widthField.getText()));
                            //canvas.setHeight(Integer.parseInt(resizeWindow.heightField.getText()));

                            graphicsContext.clearRect(0, 0, image.getWidth(), image.getHeight());
                            //canvas.setWidth(image.getWidth());
                            //canvas.setHeight(image.getHeight());

                            graphicsContext.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());

                            curvesWindow.stage.close();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                });
            }
        });

        resizeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                setResize();
            }
        });
        
        //layerContainer.addEventFilter(MouseEvent.ANY, e -> {

        //});
        
        /**
         * *click and drag**
         */
        EventHandler<MouseEvent> OnMousePressedEventHandler
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (drag.isSelected()) {
                    mouseClickCanvas(t);
                } else if (eraser.isSelected()){
                    //drawCanvasClick(t);
                    snap = makeSnapshot();   
                    autosave(snap, "notRedo");
                } else if (ZoomIn.isSelected()) {
                    zoomInClickHandle(t);
                } else if (ZoomOut.isSelected()) {
                    zoomOutClickHandle(t);
                } else if (paintbrush.isSelected()) {
                    //drawCanvasClick(t);
                    snap = makeSnapshot();   
                    autosave(snap, "notRedo");
                } else if (dropper.isSelected()) {
                    colourDropper(t);
                } else if (rotate.isSelected()) {
                    rotateClickCanvas(t);
                } else if (select.isSelected()) {
                    graphicsContext.drawImage(selectHandler.getOldImage(), 0, 0, canvas.getWidth(), canvas.getHeight());
                    WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
                    SnapshotParameters spa = new SnapshotParameters();
                    spa.setTransform(Transform.scale(pixelScale, pixelScale));
                    selectHandler.setOldImage(canvas.snapshot(spa, writableImage));
                    selectHandler.setPoint1((int)t.getX(), (int)t.getY());
                }
            }
        };

        EventHandler<MouseEvent> OnMouseDraggedEventHandler
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (drag.isSelected()) {
                    mouseDragCanvas(t);
                         scrollPane.setPannable(true);
                    
                } else if (eraser.isSelected()){
                    scrollPane.setPannable(false);
                    //drawCanvasDrag(t); /**previousMouse**/
                } else if (ZoomIn.isSelected()) {
                    scrollPane.setPannable(false);

                } else if (ZoomOut.isSelected()) {
                    scrollPane.setPannable(false);

                } else if (paintbrush.isSelected()) {
                    scrollPane.setPannable(false);
                    drawCanvasDrag(t); /**previousMouse**/
                } else if (rotate.isSelected()) {
                    scrollPane.setPannable(false);
                    rotateDragCanvas(t);
                }else if (select.isSelected()) {
                    scrollPane.setPannable(false);
                    selectHandler.setSelectActive(true);
                    selector(t);
                }else{
                    scrollPane.setPannable(false);
                }
            }
        };
        
        layerContainer.setOnMousePressed(OnMousePressedEventHandler);
        layerContainer.setOnMouseDragged(OnMouseDraggedEventHandler);
        
        
        colorpicker.setOnAction( e -> {
            brush = createBrush(brushMaxSize, colorpicker.getValue());
        });
        
        layerContainer.addEventFilter(MouseEvent.ANY, e-> {
           mouseLocation = new Point2D(e.getX(), e.getY());
           mousePressed = e.isPrimaryButtonDown();
            //mouseLocationX = e.getX(); //+ bounds.getMinX(); 
            //mouseLocationY = e.getY(); //+ bounds.getMinY(); //new Point2D(e.getX(), e.getY());
        });
        
        brushSize.textProperty().addListener(new ChangeListener<String>() {
        @Override
            public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
                brushMaxSize= Double.parseDouble(brushSize.getText()); //error HERE(*()#&$()#&$*)&#$*&#)*$)#$&$)#&$)(*#&$)(&$)($#&()#
                brushWidthHalf = brush.getWidth() / 2.0;
                brushHeightHalf = brush.getHeight() / 2.0;
                brush = createBrush(brushMaxSize, colorpicker.getValue());
            }
        });
        //addListeners();
        //startBrushHandler();

        layerContainer.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent e) {
                zoomScrollHandle(e);
                scrollPane.setVvalue(0); //stop scroll wheel moving image
            }
        });
        
        /**for (int i=0; i < brushVariations.length; i++){
            double size2 = (brushMaxSize - 1) / (double) brushVariations.length * (double) i+1;
            brushVariations[i] = createBrush(size2, colorpicker.getValue());
        } */
    }

    /**
     * *************************************************************
     *************************Functions Below***********************
     * *************************************************************
     */
    
    /**
    * fileRibbonHandler.
    * <p>
    * Method makes the file ribbon menu visible and the rest invisible.
    * <p>
    */
    public void fileRibbonHandler() {
        if (!file.isSelected()) {
            if (fileActive == true) {
                topContainer.getChildren().remove(fileBar);
                fileBar.setVisible(false);
                fileActive = false;
            } else if (fileActive == false) {
                topContainer.getChildren().remove(effectsBar);
                topContainer.getChildren().remove(toolbar);
                topContainer.getChildren().remove(editBar);
                topContainer.getChildren().add(fileBar);
                fileBar.setVisible(true);
                editBar.setVisible(false);
                effectsBar.setVisible(false);
                toolbar.setVisible(false);
                fileActive = true;
                editActive = false;
                toolboxActive = false;
                effectsActive = false;
                
            }
        } else if (file.isSelected()) {
            if (fileActive == true) {
                topContainer.getChildren().remove(fileBar);
                fileBar.setVisible(false);
                fileActive = false;
            } else if (fileActive == false) {
                topContainer.getChildren().remove(effectsBar);
                topContainer.getChildren().remove(toolbar);
                topContainer.getChildren().remove(editBar);
                topContainer.getChildren().add(fileBar);
                fileBar.setVisible(true);
                editBar.setVisible(false);
                effectsBar.setVisible(false);
                toolbar.setVisible(false);
                fileActive = true;
                editActive = false;
                toolboxActive = false;
                effectsActive = false;
            }
        }
        
    }

    /**
    * editRibbonHandler
    * <p>
    * Method makes the edit ribbon menu visible and the rest invisible. 
    * <p>
    */
    public void editRibbonHandler() {
        if (!edit.isSelected()) {
            if (editActive == true) {
                topContainer.getChildren().remove(editBar);
                editBar.setVisible(false);
                editActive = false;
            } else if (editActive == false) {
                topContainer.getChildren().remove(effectsBar);
                topContainer.getChildren().remove(toolbar);
                topContainer.getChildren().remove(fileBar);
                topContainer.getChildren().add(editBar);
                fileBar.setVisible(false);
                editBar.setVisible(true);
                effectsBar.setVisible(false);
                toolbar.setVisible(false);
                editActive = true;
                fileActive = false;
                toolboxActive = false;
                effectsActive = false;
            }
        } else if (edit.isSelected()) {
            if (editActive == true) {
                topContainer.getChildren().remove(editBar);
                editBar.setVisible(false);
                editActive = false;
            } else if (editActive == false) {
                topContainer.getChildren().remove(effectsBar);
                topContainer.getChildren().remove(toolbar);
                topContainer.getChildren().remove(fileBar);
                topContainer.getChildren().add(editBar);
                fileBar.setVisible(false);
                editBar.setVisible(true);
                effectsBar.setVisible(false);
                toolbar.setVisible(false);
                editActive = true;
                fileActive = false;
                toolboxActive = false;
                effectsActive = false;
            }
        }
       
    }
    
    /**
    * effectsRibbonHandler
    * <p>
    * Method makes the effects ribbon menu visible and the rest invisible. 
    * <p>
    */
    public void effectsRibbonHandler() {
        if (!effects.isSelected()) {
            if (effectsActive == true) {
                topContainer.getChildren().remove(effectsBar);
                effectsBar.setVisible(false);
                effectsActive = false;
            } else if (effectsActive == false) {
                topContainer.getChildren().remove(fileBar);
                topContainer.getChildren().remove(toolbar);
                topContainer.getChildren().remove(editBar);
                topContainer.getChildren().add(effectsBar);
                fileBar.setVisible(false);
                editBar.setVisible(false);
                effectsBar.setVisible(true);
                toolbar.setVisible(false);
                effectsActive = true;
                fileActive = false;
                editActive = false;
                toolboxActive = false;
            }
        } else if (effects.isSelected()) {
            if (effectsActive == true) {
                topContainer.getChildren().remove(effectsBar);
                effectsBar.setVisible(false);
                effectsActive = false;
            } else if (effectsActive == false) {
                topContainer.getChildren().remove(fileBar);
                topContainer.getChildren().remove(toolbar);
                topContainer.getChildren().remove(editBar);
                topContainer.getChildren().add(effectsBar);
                fileBar.setVisible(false);
                editBar.setVisible(false);
                effectsBar.setVisible(true);
                toolbar.setVisible(false);
                effectsActive = true;
                fileActive = false;
                editActive = false;
                toolboxActive = false;
            }
        }
    }
    
    /**
    * toolbarRibbonHandler
    * <p>
    * Method makes the toolbar ribbon menu visible and the rest invisible. 
    * <p>
    */
    public void toolbarRibbonHandler() {
        if (!toolsel.isSelected()) {
            if (toolboxActive == true) {
                topContainer.getChildren().remove(toolbar);
                toolbar.setVisible(false);
                toolboxActive = false;
            } else if (toolboxActive == false) {
                topContainer.getChildren().remove(fileBar);
                topContainer.getChildren().remove(effectsBar);
                topContainer.getChildren().remove(editBar);
                topContainer.getChildren().add(toolbar);
                fileBar.setVisible(false);
                editBar.setVisible(false);
                effectsBar.setVisible(false);
                toolbar.setVisible(true);
                toolboxActive = true;
                effectsActive = false;
                fileActive = false;
                editActive = false;
            }
        } else if (toolsel.isSelected()) {      
            if (toolboxActive == true) {
                topContainer.getChildren().remove(toolbar);
                toolbar.setVisible(false);
                toolboxActive = false;
            } else if (toolboxActive == false) {
                topContainer.getChildren().remove(fileBar);
                topContainer.getChildren().remove(effectsBar);
                topContainer.getChildren().remove(editBar);
                topContainer.getChildren().add(toolbar);
                fileBar.setVisible(false);
                editBar.setVisible(false);
                effectsBar.setVisible(false);
                toolbar.setVisible(true);
                toolboxActive = true;
                effectsActive = false;
                fileActive = false;
                editActive = false;
            }         
        }
        

    }

    /**
     *
     * @param t
     */
    public void zoomInClickHandle(MouseEvent t) {
        ZoomHandler zoomOperator = new ZoomHandler();
        ZoomHandler zoomOperator2 = new ZoomHandler();
        double zoomFactor = 1.5;
        //zoomOperator.zoom(layerContainer ,zoomFactor, t.getSceneX(), t.getSceneY());
        //zoomOperator2.zoom(underCanvas, zoomFactor, t.getSceneX(), t.getSceneY());
       
        //tom cvs zoom test
        float zooom = 2;
        
        canvas.setScaleX(zooom);
        canvas.setScaleY(zooom);
        System.out.println("scaling!!!");
      
    }
    

    
    /**
     *
     */
    public void zoomLoader(){
        double Value = canvas.getScaleY();
        String valueC = (String.valueOf(Value));
        heightLabel.textProperty().setValue(valueC);
        double Value2 = canvas.getScaleX();
        String valueD = (String.valueOf(Value2));
        widthLabel.textProperty().setValue(valueD);
    }

    /**
     *
     * @param t
     */
    public void zoomOutClickHandle(MouseEvent t) {
        ZoomHandler zoomOperator = new ZoomHandler();
        ZoomHandler zoomOperator2 = new ZoomHandler();
        double zoomFactor = 0.75;
        zoomOperator.zoom(layerContainer, zoomFactor, t.getSceneX(), t.getSceneY());
        //zoomOperator2.zoom(underCanvas, zoomFactor, t.getSceneX(), t.getSceneY());
    }
    
    /**
     *
     * @param t
     */
    public void defaultZoom(ActionEvent t) {
        ZoomHandler zoomOperator = new ZoomHandler();
        zoomOperator.defaultZoom(layerContainer, imageWidth, imageHeight);
        //zoomOperator2.zoom(underCanvas, zoomFactor, t.getSceneX(), t.getSceneY());
    }
    
    /**
     *
     * @param t
     */
    public void savedMZoom(ActionEvent t) {
        scaleWidth = canvas.getScaleX();
        ZoomHandler zoomOperator = new ZoomHandler();
        zoomOperator.defaultZoomInstant(layerContainer);
        //zoomOperator2.zoom(underCanvas, zoomFactor, t.getSceneX(), t.getSceneY());
    }

    /**
     *
     * @param e
     */
    public void zoomScrollHandle(ScrollEvent e) {
        
        //ZoomHandler zoomOperator2 = new ZoomHandler();
        double zoomFactor = 1.5;
        double zoomLimitCap = 25;
        double zoomLimitBottom = 0;
            if (e.getDeltaY() >= 0) {
                if(zoomCounter == zoomLimitBottom){
                    zoomOperator.zoom(layerContainer, zoomFactor, e.getSceneX(), e.getSceneY());
                    zoomCounter++;
                    zoomLoader();
                }
                else if(zoomCounter == zoomLimitCap){

                } else{
                    zoomOperator.zoom(layerContainer, zoomFactor, e.getSceneX(), e.getSceneY());
                    zoomCounter++;
                    zoomLoader();
                }
            } 
        
            if(e.getDeltaY() <= 0){
                if(zoomCounter == zoomLimitCap){
                    zoomFactor = 1 / zoomFactor;
                    zoomOperator.zoom(layerContainer, zoomFactor, e.getSceneX(), e.getSceneY());
                    zoomCounter--;
                    zoomLoader();
                }
                else if(zoomCounter == zoomLimitBottom){
                
                } else{
                    zoomFactor = 1 / zoomFactor;
                    zoomOperator.zoom(layerContainer, zoomFactor, e.getSceneX(), e.getSceneY());
                    zoomCounter--;
                    zoomLoader();
                }
            }
        
        //zoomOperator2.zoom(underCanvas, zoomFactor, e.getSceneX(), e.getSceneY());
    }

    /**public void drawCanvasClick(MouseEvent t) {
        double size = Double.parseDouble(brushSize.getText()); //error HERE(*()#&$()#&$*)&#$*&#)*$)#$&$)#&$)(*#&$)(&$)($#&()#
        double x = t.getX() - size / 2;
        double y = t.getY() - size / 2;
        if (eraser.isSelected()) {
            if (size == 0){
                graphicsContext.clearRect(x, y, 8, 8);
            } else {
                graphicsContext.clearRect(x, y, size, size);
            }
        } else {
            if (size == 0){
                graphicsContext.setFill(colorpicker.getValue());
                graphicsContext.fillRect(x, y, 8, 8);
            } else {
                graphicsContext.setFill(colorpicker.getValue());
                graphicsContext.fillRect(x, y, size, size);
            }
        }
    }**/
    
           /* private void addListeners() {

*/


    //}
    
    
    
    public void drawCanvasDrag(MouseEvent t) { 
        
        //double x = t.getX();
        //double y = t.getY();
        //Bounds bounds = canvas.localToScene(canvas.getBoundsInLocal());
        
        
        //prevX = t.getX();
        //prevY = t.getY(); // = new Point2D( mouseLocationX, mouseLocationY);
                    
        loop = new AnimationTimer() {
            @Override
                public void handle(long now){
                    if(mousePressed){
                        if (eraser.isSelected()) {
                            //graphicsContext.clearRect(x, y, size, size);
                            //bresenhamAlgorithm(prevX, prevY, x, y);
                            bresenhamAlgorithm((int)prev.getX(), (int)prev.getY(), ((int)mouseLocation.getX()), ((int)mouseLocation.getY()));
                        } else if (paintbrush.isSelected()){
                            //graphicsContext.setFill(colorpicker.getValue());
                            //graphicsContext.fillRect(x, y, size, size);
                            bresenhamAlgorithm((int)prev.getX(), (int)prev.getY(), ((int)mouseLocation.getX()), ((int)mouseLocation.getY()));
                            
                            /**pressure += pressureDelay;
                            if(pressure > 1){
                                pressure = 1;
                            }
                            else{
                                pressure = 0;
                            }*/
                        }
                    }
                    prev = new Point2D(mouseLocation.getX(), mouseLocation.getY());
                    //prev = new Point2D(t.getX(), t.getY());
                    
            }
        };
        loop.start();
    }
    

    private void selector(MouseEvent t){
        
        System.out.println("X: " + t.getX());
        System.out.println("Y: " + t.getY());
        
        
        
        selectHandler.setPoint2((int)t.getX(), (int)t.getY());
        Image newImage = selectHandler.drawBox(selectHandler.getOldImage());
                
                
        resetImage();
        graphicsContext.drawImage(newImage, 0, 0, canvas.getWidth(), canvas.getHeight());
            
    }
    
    
    /*private void startBrushHandler(){
        //double size = Double.parseDouble(brushSize.getText()); //error HERE(*()#&$()#&$*)&#$*&#)*$)#$&$)#&$)(*#&$)(&$)($#&()#
        //double size = Double.parseDouble(brushSize.getText());
        //double x = t.getX() - size / 2;
        //double y = t.getY() - size / 2;
    }*/

    
    //}
    // mouseClickCanvas(t); For Drag Tool

    /**
     *
     * @param t
     */
    public void mouseClickCanvas(MouseEvent t) {
        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();
        orgTranslateX = layerContainer.getTranslateX();
        orgTranslateY = layerContainer.getTranslateY();
    }

    //mouseDragCanvas(t); For Drag Tool

    /**
     *
     * @param t
     */
    public void mouseDragCanvas(MouseEvent t) {
        double offsetX = t.getSceneX() - orgSceneX;
        double offsetY = t.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;
        layerContainer.setTranslateX(newTranslateX);
        layerContainer.setTranslateY(newTranslateY);
        //underCanvas.setTranslateX(newTranslateX);
        //underCanvas.setTranslateY(newTranslateY);
    }

    // rotateClickCanvas(t); For Drag Tool

    /**
     *
     * @param t
     */
    public void rotateClickCanvas(MouseEvent t) {
        orgSceneX = t.getSceneX();
        orgSceneY = t.getSceneY();
      //  orgTranslateX = canvas.getTranslateX();
      //  orgTranslateY = canvas.getTranslateY();
    }
    
    /*public void bresenhamAlgorithm(double xold, double yold, double newx, double newy)
    {
        double deltax = Math.abs(newx - xold);
        double deltay = -Math.abs(newy - yold);
        double alphax = xold < newx ? 1. : -1.; 
	double alphay = yold < newy ? 1. : -1.; 
        double deltaerr = deltax+deltay;
        double error;
        while(true){
            graphicsContext.fillRect(xold, yold, 8, 8);
            if(xold == newx && yold == newy)break;
                error = 2.*deltaerr;
                if(error > deltay){ error += deltay; xold += alphax; }
                if(error < deltax){ error += deltax; yold += alphay; }
        }
    }*/
    
    private void bresenhamAlgorithm(double x0, double y0, double x1, double y1)
    {
      double dx =  Math.abs(x1-x0), sx = x0<x1 ? 1. : -1.;
      double dy = -Math.abs(y1-y0), sy = y0<y1 ? 1. : -1.;
      double err = dx+dy, e2; /* error value e_xy */
      
      //Bounds bounds = canvas.localToScene(canvas.getBoundsInLocal());
      while( true){
            if (eraser.isSelected()) {
                graphicsContext.clearRect(x0/*+bounds.getMinX()*/, y0/*+bounds.getMinY()*/, brushMaxSize, brushMaxSize);
            } else if (paintbrush.isSelected()){
                //int variation = (int) (pressure * (brushVariations.length - 1));
                //Image brushVariation = brushVariations[ variation ];
                graphicsContext.setFill(colorpicker.getValue());
                //graphicsContext.fillRect(x0/*+bounds.getMinX()*/, y0/*+bounds.getMinY()*/, size, size);
                //graphicsContext.setGlobalAlpha(pressure);
                graphicsContext.drawImage(brush , x0 - brushWidthHalf, y0 - brushHeightHalf); //(brushVariation)
            }
        if (x0==x1 && y0==y1) break;
        e2 = 2.*err;
        
        if (e2 > dy) { 
            err += dy; 
            x0 += sx; } /* e_xy+e_x > 0 */
        
        if (e2 < dx) { 
            err += dx; 
            y0 += sy; } /* e_xy+e_y < 0 */
      }
    }
    
    /**
     *
     * @param node
     * @return
     */
    public static Image createImage(Node node) {

        WritableImage wi;

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        int imageWidth = (int) node.getBoundsInLocal().getWidth();
        int imageHeight = (int) node.getBoundsInLocal().getHeight();

        wi = new WritableImage(imageWidth, imageHeight);
        node.snapshot(parameters, wi);

        return wi;

    }
    
    /**
     *
     * @param radius
     * @param color
     * @return
     */
    public static Image createBrush( double radius, Color color) {

        // create gradient image with given color
        Circle brush = new Circle(radius);

        RadialGradient gradient1 = new RadialGradient(0, 0, 0, 0, radius, false, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(1, 1, 1, 0.3)), new Stop(1, color.deriveColor(1, 1, 1, 0)));

        brush.setFill(gradient1);

        // create image
        return createImage(brush);

    }


    //rotateDragCanvas(t); For Drag Tool

    /**
     *RotateDragCanvas. Rotate the canvas based on a click and drag
     * @param t
     */
    public void rotateDragCanvas(MouseEvent t) {
        //mouseLocation = new Point2D(0,0);
        //prev = new Point2D(0,0);
        double offsetX = t.getSceneX() - orgSceneX;
        double offsetY = t.getSceneY() - orgSceneY;

       // double newTranslateX = orgTranslateX + offsetX;
       //double newTranslateY = orgTranslateY + offsetY;

       layerContainer.setRotate(layerContainer.getRotate() + offsetX);
        //canvas.setRotate((offsetX/10));
        rotateClickCanvas(t);
    }

    /**
     *
     */
    public void rotateClockwise() {
        double size = Double.parseDouble(rotateDegree.getText());
        layerContainer.setRotate(layerContainer.getRotate() + size);
    }

    /**
    * rotateAntiClockwise
    * <p>
    *  Rotate the image by the specified number of degrees in rotateDegree.getText();
    * <p>
    */
    public void rotateAntiClockwise() {
        double size = Double.parseDouble(rotateDegree.getText());
        layerContainer.setRotate(layerContainer.getRotate() - size);
    }
    
    /**
    * resetRoatation
    * <p>
    * reset the rotation of the layerContainer to 0.
    * <p>
    */
    public void resetRotation(){
        layerContainer.setRotate(0);
    }

    /**
     *
     */
    public void setContrast() {
        snap = makeSnapshot();
        double tempValue = effect.getContrast();
        ContrastWindow contrastWindow = new ContrastWindow();
        contrastWindow.slider.setValue(tempValue * 100);
        contrastWindow.label.setText(String.valueOf(tempValue * 100));
        contrastWindow.showStage();
        contrastWindow.slider.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                contrastWindow.label.textProperty().setValue(
                        String.valueOf((int) contrastWindow.slider.getValue()));
                effect = new ColorAdjust(effect.getHue(), effect.getSaturation(), effect.getBrightness(), (contrastWindow.slider.getValue()) / 100);
                canvas.setEffect(effect);
            }
        });
        contrastWindow.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                effect = new ColorAdjust(effect.getHue(), effect.getSaturation(), effect.getBrightness(), tempValue);
                canvas.setEffect(effect);
            }
        });
        contrastWindow.button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                contrastWindow.stage.close();
                autosave(snap, "notRedo");
            }
        });
    }

    /**
     *
     */
    public void setResize(){
        ResizeEffect resizeEffect = new ResizeEffect();
        ResizeWindow resizeWindow = new ResizeWindow();

        ////
        resizeWindow.showStage();

        resizeWindow.button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
                    SnapshotParameters spa = new SnapshotParameters();
                    spa.setTransform(Transform.scale(pixelScale, pixelScale));
                    //return canvas.snapshot(spa, writableImage);
                    Image image = canvas.snapshot(spa,null);

                    //Image image = canvas.snapshot(new SnapshotParameters(), null);


                    image = resizeEffect.main(image, Integer.parseInt(resizeWindow.widthField.getText()), Integer.parseInt(resizeWindow.heightField.getText()));
                    resetImage();

                    canvas.setWidth(Integer.parseInt(resizeWindow.widthField.getText()));
                    canvas.setHeight(Integer.parseInt(resizeWindow.heightField.getText()));

                    graphicsContext.clearRect(0, 0, image.getWidth(), image.getHeight());
                    //canvas.setWidth(image.getWidth());
                    //canvas.setHeight(image.getHeight());

                    graphicsContext.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());

                    resizeWindow.stage.close();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });
        ////
    }

    /**
     *
     */
    public void setBrightness() {
        snap = makeSnapshot();
        double tempValue = effect.getBrightness();
        BrightnessWindow brightnessWindow = new BrightnessWindow();
        brightnessWindow.slider.setValue(tempValue * 100);
        brightnessWindow.label.setText(String.valueOf(tempValue * 100));
        brightnessWindow.showStage();
        brightnessWindow.slider.valueProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                brightnessWindow.label.textProperty().setValue(
                        String.valueOf((int) brightnessWindow.slider.getValue()));
                effect = new ColorAdjust(effect.getHue(), effect.getSaturation(), (brightnessWindow.slider.getValue() / 100), effect.getContrast());								//
                canvas.setEffect(effect);
            }
        });
        brightnessWindow.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                effect = new ColorAdjust(effect.getHue(), effect.getSaturation(), tempValue, effect.getContrast());
                canvas.setEffect(effect);
            }
        });

        brightnessWindow.button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                brightnessWindow.stage.close();
                autosave(snap, "notRedo");
            }
        });
    }

    /**
     *
     */
    public void setMonochrome() {
        snap = makeSnapshot();
        double tempValue = effect.getSaturation();
        MonochromeWindow monochromeWindow = new MonochromeWindow();
        monochromeWindow.slider.setValue(tempValue * 100);
        monochromeWindow.label.setText(String.valueOf(tempValue * 100));
        monochromeWindow.showStage();
        monochromeWindow.slider.valueProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                monochromeWindow.label.textProperty().setValue(
                        String.valueOf((int) monochromeWindow.slider.getValue()));
                effect = new ColorAdjust(effect.getHue(), (monochromeWindow.slider.getValue() / 100), effect.getBrightness(), effect.getBrightness());								//
                canvas.setEffect(effect);
            }
        });
        monochromeWindow.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                effect = new ColorAdjust(effect.getHue(), tempValue, effect.getBrightness(), effect.getContrast());
                canvas.setEffect(effect);
            }
        });

        monochromeWindow.button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                monochromeWindow.stage.close();
                autosave(snap, "notRedo");
            }
        });	
    }

    /**
     *
     */
    public void setColourize() {
        snap = makeSnapshot();
        double tempValue = effect.getHue();
        ColourizeWindow colourizeWindow = new ColourizeWindow();
        colourizeWindow.slider.setValue(tempValue * 100);
        colourizeWindow.label.setText(String.valueOf(tempValue * 100));
        colourizeWindow.showStage();
        colourizeWindow.slider.valueProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                colourizeWindow.label.textProperty().setValue(
                        String.valueOf((int) colourizeWindow.slider.getValue()));
                effect = new ColorAdjust(colourizeWindow.slider.getValue() / 100, effect.getSaturation(), effect.getBrightness(), effect.getBrightness());								//
                canvas.setEffect(effect);
            }
        });
        colourizeWindow.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                effect = new ColorAdjust(tempValue, effect.getSaturation(), effect.getBrightness(), effect.getContrast());
                canvas.setEffect(effect);
            }
        });

        colourizeWindow.button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                colourizeWindow.stage.close();
                autosave(snap, "notRedo");
            }
        });	
    }
    
    public void setPixelise(){
        snap = makeSnapshot();
        WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(pixelScale, pixelScale));
        Image image = canvas.snapshot(spa, writableImage);
        
        PixeliseWindow pixeliseWindow = new PixeliseWindow();
        pixeliseWindow.slider.setValue(1);
        pixeliseWindow.label.setText(String.valueOf(1));
        pixeliseWindow.showStage();
        
        pixeliseWindow.slider.valueProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                pixeliseWindow.label.textProperty().setValue(
                        String.valueOf((int) pixeliseWindow.slider.getValue()));
                
                PixeliseEffect pixeliseEffect = new PixeliseEffect();

                
                
                
                 if(selectHandler.getSelectActive() == false){   
                    Image newImage;
                    newImage = pixeliseEffect.main(image, (int)pixeliseWindow.slider.getValue());
                    resetImage();
                    graphicsContext.drawImage(newImage, 0, 0, canvas.getWidth(), canvas.getHeight());
                    }else{
                        Image newImage = selectHandler.getSelection();
                        newImage = pixeliseEffect.main(newImage, (int)pixeliseWindow.slider.getValue() - 1);
                    
                        resetImage();
                        graphicsContext.drawImage(selectHandler.getOldImage(), 0, 0);
                        graphicsContext.drawImage(newImage, selectHandler.getStartX(), selectHandler.getStartY());
                        
                    }
                
            }
        });
        
        pixeliseWindow.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                graphicsContext.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight());
            }
        });

        pixeliseWindow.button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                selectHandler.setOldImage(canvas.snapshot(spa, writableImage));
                selectHandler.setSelectActive(false);
                pixeliseWindow.stage.close();
                autosave(snap, "notRedo");
            }
        });
    }

    /**
     *
     * @param t
     */
    public void colourDropper(MouseEvent t) {
        int x = (int)t.getX();
        int y = (int)t.getY();
        Color selection = new Color(0, 0, 0, 1);
        selection = canvas.snapshot(new SnapshotParameters(), null).getPixelReader().getColor(x, y);
        colorpicker.setValue(selection);
    }
    
    /**
     *
     */
    public void load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open...");
        FileChooser.ExtensionFilter extFilterIMG = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpg)", "*.JPG", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterIMG); 		// Sets the extensions able to be selected by the FileChooser
        String dir = System.getProperty("user.dir");
        File defDirectory = new File(dir + "/Outputs"); 	// Sets the default directory
        fileChooser.setInitialDirectory(defDirectory);
        File file = fileChooser.showOpenDialog(null);
        filename = file.getName();
        System.out.println(filename);
        try {
            layerContainer.getChildren().remove(canvas);
            layerContainer.getChildren().remove(underCanvas);
            Image image = new Image(file.toURI().toString());
            resetImage();
            graphicsContext.clearRect(0, 0, 300, 300);
            
            //ZoomHandler zoomHandler = new ZoomHandler();
            //zoomHandler.defaultZoom(canvas, underCanvas, imageWidth, imageHeight);
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            scene2.setWidth(imageWidth);
            scene2.setHeight(imageHeight);
            canvas.setWidth(imageWidth);
            canvas.setHeight(imageHeight);
            underCanvas.setWidth(imageWidth);
            underCanvas.setHeight(imageHeight);
            drawUnderTransparency((int)imageWidth, (int)imageHeight);
            layerContainer.getChildren().add(canvas);
            layerContainer.getChildren().add(underCanvas);
            canvas.toFront();
            underCanvas.toBack();
            
            graphicsContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
            //primaryStage.setTitle(filename);
            //primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
        iteration = 0;
        undoCount = 0;
        redoCount = 0;
        firstRedo = true;
    }

    /**
     *
     */
    public void newCanvas() {
        NewFileWindow fileWindow = new NewFileWindow();
        fileWindow.showStage();
        fileWindow.button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    layerContainer.getChildren().remove(canvas);
                    layerContainer.getChildren().remove(underCanvas);
                    String tempX = fileWindow.sizeX.getText();
                    String tempY = fileWindow.sizeY.getText();
                    double dX = Double.parseDouble(tempX);
                    double dY = Double.parseDouble(tempY);

                    graphicsContext.setFill(Color.GREEN);

                    canvas.setWidth(dX);
                    underCanvas.setWidth(dX);
                    canvas.setHeight(dY);
                    underCanvas.setHeight(dY);
                    graphicsContext.fillRect(0, 0, dX, dY); ///temporarily removed

                    drawUnderTransparency((int)dX, (int)dY);
                    layerContainer.getChildren().add(canvas);
                    layerContainer.getChildren().add(underCanvas);
                    canvas.toFront();
                    underCanvas.toBack();
                    fileWindow.stage.close();
                    filename = "";
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });
        iteration = 0;
        undoCount = 0;
        redoCount = 0;
        firstRedo = true;
    }

    /**
     *
     * @param newX
     * @param newY
     */
    public void drawUnderTransparency(int newX, int newY){
        boolean grey = false;
        boolean initialGrey = false;

        for(int row=0; row < newX; row = row + 10){
            initialGrey = !initialGrey;
            grey = initialGrey;
            for(int col=0; col < newY; col = col + 10){ 
                if(grey == true) {
                    graphicsContextUnder.setFill(Color.GREY);
                    grey = false;
                } else {
                    graphicsContextUnder.setFill(Color.rgb(1, 1, 1, 0.2));
                    grey = true;
                }
                graphicsContextUnder.fillRect(row,col,20,20);
            }
            //graphicsContextUnder.setFill(Color.AQUAMARINE);
            //graphicsContextUnder.fillRect(0, 0, newX, newY);
        }
    }
    
    /**public void drawUnderTransparency(int newX, int newY){
        int col;
        int row;
        int x,y;
        for(row=0; row < newX; row++){
            for(col=0; col < newY; col++){
                x = col * 3;
                y = row * 3;
                if((row % 2) == (col % 2)) {
                    graphicsContextUnder.setFill(Color.GREY);
                } else {
                    graphicsContextUnder.setFill(Color.rgb(1, 1, 1, 0.2));
                }
                graphicsContextUnder.fillRect(x,y,6,6);
            }
        }
    }**/
    

    /**
     *
     */
    public void save() {
        if (filename == "") {
            saveAs();
        } else { 
            String location = System.getProperty("user.dir");
            location = location + "/Outputs/";
            File output = new File(location + filename);

            try {
                double scale = canvas.getScaleX();
                ZoomHandler zoomHandler = new ZoomHandler();
                zoomHandler.defaultZoomInstant(layerContainer);
                
                //int intX = (int)canvas.getWidth();
                //int intY = (int)canvas.getHeight();
                //WritableImage writableImage = new WritableImage(intX,intY);
                //canvas.snapshot(null, writableImage);
                //RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage,null);
                //ImageIO.write(renderedImage, "png", output);
               
                WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
                SnapshotParameters spa = new SnapshotParameters();
                spa.setFill(Color.TRANSPARENT);// Sets a transparent layer, to be used as bg
                spa.setTransform(Transform.scale(pixelScale, pixelScale));
                //return canvas.snapshot(spa, writableImage);
                ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(spa,writableImage),null), "png", output);
                
                
                //ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(parameters, null), null), "png", output);
                zoomHandler.applyZoomInstant(layerContainer, scale);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPEG File", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG File", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG); 		// Sets the extensions able to be selected by the FileChooser
        //File defDirectory = new File(System.getProperty("user.dir")); 	// Sets the default directory
        String dir = System.getProperty("user.dir");
        File defDirectory = new File(dir + "/Outputs");
        fileChooser.setInitialDirectory(defDirectory);
        File file = fileChooser.showSaveDialog(null);
       
        if (file != null) {
            try {
                double scale = canvas.getScaleX();
                ZoomHandler zoomHandler = new ZoomHandler();
                zoomHandler.defaultZoomInstant(layerContainer);
                //ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(parameters, null), null), "png", file); Old code
                // New saving code where Pixel Density applies.
                WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
                SnapshotParameters spa = new SnapshotParameters();
                spa.setFill(Color.TRANSPARENT);
                spa.setTransform(Transform.scale(pixelScale, pixelScale));
                //return canvas.snapshot(spa, writableImage);
                ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(spa,writableImage),null), "png", file);
                // end new saving code.
                zoomHandler.applyZoomInstant(layerContainer, scale);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void undo() {
        if (iteration > 0) {
            if (undoCount > 0) {
                snap = makeSnapshot();   
                autosave(snap, "redo");
                
                quickLoad(undoCount - 1);
                undoCount--;
                if (redoCount < 9) {
                    redoCount++;
                }
            } else if (undoCount == 0) {
                snap = makeSnapshot();   
                autosave(snap, "redo");
                    
                quickLoad(9);
                undoCount = 9;
                if (redoCount < 9) {
                    redoCount++;
                }
            }
            iteration--;
        }
    }
    
    public void redo() {
        if (redoCount > 0) {
            quickLoad(undoCount+1);
            undoCount++;
            redoCount--;
            iteration++;
        }
    }
    
    public WritableImage makeSnapshot() {
        WritableImage writableImage = new WritableImage((int)Math.rint(pixelScale*canvas.getWidth()), (int)Math.rint(pixelScale*canvas.getHeight()));
        SnapshotParameters spa = new SnapshotParameters();
        spa.setFill(Color.TRANSPARENT);// Sets a transparent layer, to be used as bg
        spa.setTransform(Transform.scale(pixelScale, pixelScale));
        
        return canvas.snapshot(spa,writableImage);
    }
    
    public void autosave(WritableImage im, String type) {
        String location = System.getProperty("user.dir");
            location = location + "/cache/";
            File output = new File(location + "throwback " + undoCount + ".png");

            try {
                double scale = canvas.getScaleX();
                ZoomHandler zoomHandler = new ZoomHandler();
                zoomHandler.defaultZoomInstant(layerContainer);
                
                //int intX = (int)canvas.getWidth();
                //int intY = (int)canvas.getHeight();
                //WritableImage writableImage = new WritableImage(intX,intY);
                //canvas.snapshot(null, writableImage);
                //RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage,null);
                //ImageIO.write(renderedImage, "png", output);
                
                ImageIO.write(SwingFXUtils.fromFXImage(im,null), "png", output);
                
                
                //ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(parameters, null), null), "png", output);
                zoomHandler.applyZoomInstant(layerContainer, scale);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            if (type != "redo") {
                iteration++;
                if (undoCount < 9) {
                    undoCount++;
                } else if (undoCount == 9) {
                    undoCount = 0;
                }
            }
    }
    
    public void quickLoad(int version) {
        String location = System.getProperty("user.dir");
        location = location + "/cache/";
        File file = new File(location + "throwback " + version + ".png");
        try {
            layerContainer.getChildren().remove(canvas);
            layerContainer.getChildren().remove(underCanvas);
            Image image = new Image(file.toURI().toString());
            resetImage();
            graphicsContext.clearRect(0, 0, 300, 300);
            
            //ZoomHandler zoomHandler = new ZoomHandler();
            //zoomHandler.defaultZoom(canvas, underCanvas, imageWidth, imageHeight);
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            scene2.setWidth(imageWidth);
            scene2.setHeight(imageHeight);
            canvas.setWidth(imageWidth);
            canvas.setHeight(imageHeight);
            underCanvas.setWidth(imageWidth);
            underCanvas.setHeight(imageHeight);
            drawUnderTransparency((int)imageWidth, (int)imageHeight);
            layerContainer.getChildren().add(canvas);
            layerContainer.getChildren().add(underCanvas);
            canvas.toFront();
            underCanvas.toBack();
            
            graphicsContext.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
            //primaryStage.setTitle(filename);
            //primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void settings(){
        SettingsWindow settingsWindow = new SettingsWindow();
        settingsWindow.showStage();
        
        
        settingsWindow.button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                    ThemeProperty themeProperty = new ThemeProperty();
                switch (settingsWindow.theme.intValue()) {
                    case 0:
                        scene.getStylesheets().add(getClass().getResource("defaultTheme.css").toExternalForm());
                        themeProperty.SaveProperty("0");
                        break;
                    case 1:
                        scene.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
                        themeProperty.SaveProperty("1");
                        break;
                    case 2:
                        scene.getStylesheets().add(getClass().getResource("blueTheme.css").toExternalForm());
                        themeProperty.SaveProperty("2");
                        break;
                    case 3:
                        scene.getStylesheets().add(getClass().getResource("largeTextTheme.css").toExternalForm());
                        themeProperty.SaveProperty("3");
                        break;
                    default:
                        break;
                }
                
                settingsWindow.stage.close();
            }
        });
        

    }
    /**
     *
     */
    public void resetImage() {
        effect = new ColorAdjust(0, 0, 0, 0);	// Resets all of the image alterations
        canvas.setEffect(effect);					// whenever a new image is loaded
        canvas.setRotate(0);
    }

    /**
     *
     */
    public void exitProgram() {
        Alert alert = new Alert(AlertType.CONFIRMATION);				// Creates an alert box when
        alert.setTitle("Exit Program");									// the user attempts to exit
        alert.setHeaderText("Are you sure you wish to exit?");			// the program
        alert.setContentText("Press OK to exit, or Cancel to return");	//
        Optional<ButtonType> result = alert.showAndWait();		// Closes the program if the
        if (result.get() == ButtonType.OK) {					// "OK" button is pressed,
            Stage stage = (Stage) scene.getWindow();	// or returns to the program
            stage.close();										// if the user selects "Cancel"
        } else {

        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}

