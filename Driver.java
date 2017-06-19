//The first assignment simulator for calculator 
//created: 6/5/2017 
//Author: Shervin Shahidizandi

package assignment1;


import java.util.HashMap;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * @author Shervin Shahidzandi
 * Date created: 6/5/2017
 * Last version data: 6/8/2017
 *
 *This is a small simple calculator doing the basic five operators which is using javaFx library 
 *based on assignment we have the design has been tried to be as similar 
 */
public class Driver extends Application{
    /*
     * fields needed for implementing the calculator 
     * might need to be change to be as close as what he wanted 
     */
    private static final String[][] template = {
	    {"7","8","9","%","√"},
	    {"4","5","6","x","÷"},
	    {"1","2","3","+","-"},
	    {"0",".","=","c"}
    };
    /*
     * nice trick of adding a hashMap to accelarte moving through buttons
     */
    private HashMap<String, Button> buttonMap = new HashMap<>();
    
    /*
     * 
     * for handling the double value operations
     */
    private DoubleProperty stackedValue = new SimpleDoubleProperty();
    private DoubleProperty value = new SimpleDoubleProperty();
    
    /*
     * 
     * for handling operators 
     */
    private enum Operator {
     NOOP,ADD, SUBTRACT, DIVIDE, MULTIPLY, MOD 
    }
    
    //needed for handling operators and also handling the precedence in future extension
    private Operator currentOperator = Operator.NOOP;
    private Operator stackedOperator = Operator.NOOP;
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
	final TextField screen  = createScreen();
	final TilePane  buttons = createButtons();

	primaryStage.setTitle("Calc");
	primaryStage.initStyle(StageStyle.UTILITY);
	primaryStage.setResizable(false);
	primaryStage.setScene(new Scene(buttonLayouts(screen, buttons)));
	primaryStage.show();
	/**
	 * Scene is always needed for all applications 
	 * without scene we have nothing to add to our primaryStage built 
	 * if wanted to add extra space we can give dimensions to our scene as well
	 */
	
    }
    public static void main(String[] args) {

	  launch(args);  
    }
    
    /*
     * This is the layout we create and pass our scene
     */
    private VBox buttonLayouts(TextField screen , TilePane buttons){
	//creates the all size layout of buttons at first and adds some background color for us
	VBox layout = new VBox(20);
	layout.setAlignment(Pos.CENTER);
	layout.setStyle("-fx-background-color: yellowgreen; -fx-padding: 30; -fx-font-size: 15;");
	//adding what we have as values to the actual layout
	layout.getChildren().setAll(screen, buttons);
	//for improving the overall speed of operations
	accelatorWarpper(layout);
	return layout;
    }
    
    /*
     * method for handling the accelerator we had 
     */
    private void accelatorWarpper(VBox layout){
	layout.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

	    @Override
	    public void handle(KeyEvent event) {
		Button pressed = buttonMap.get(event.getText());
		//if pressed is not null yet fire the button 
		if(pressed != null)
		    pressed.fire();
	    }
	});
    }
   /*
    * this methods creates the screen we use for screen in our start 
    */
   
	private TextField createScreen() {
	    final TextField screen = new TextField();
	    screen.setStyle("-fx-background-color: skyblue;");
	    screen.setAlignment(Pos.CENTER_RIGHT);
	    screen.setEditable(false);
	    screen.textProperty().bind(Bindings.format("%.0f", value));
	    return screen;
	  }

	  private TilePane createButtons() {
	    TilePane buttons = new TilePane();
	    buttons.setVgap(7);
	    buttons.setHgap(7);
	    buttons.setPrefColumns(template[0].length);
	    //reading the whole template we already have created
	    for (String[] r: template) {
	      for (String s: r) {
		  //creating those buttons one by one
	        buttons.getChildren().add(createButton(s));
	      }
	    }
	    return buttons;
	  }

	  private Button createButton(final String s) {
	      //default mode of creating buttons if we have something in template that can't be categorized and used in other methods
	    Button button = makeStandardButton(s);
	    
	    //for numbers
	    if (s.matches("[0-9]")) {
	      makeNumericButton(s, button);
	    } else {
	      final ObjectProperty<Operator> triggerOperator = determineOperand(s);
	      if (triggerOperator.get() != Operator.NOOP) {
	        makeOperatorerandButton(button, triggerOperator);
	      } else if ("c".equals(s)) {
	        makeClearButton(button);
	      } else if ("=".equals(s)) {
	        makeEqualsButton(button);
	      }
	    }

	    return button;
	  }
	  private ObjectProperty<Operator> determineOperand(String s) {
	      final ObjectProperty<Operator> triggerOp = new SimpleObjectProperty<>(Operator.NOOP);
	      switch (s) {
	      	case "%": triggerOp.set(Operator.MOD); 	    break;
	       	case "+": triggerOp.set(Operator.ADD);      break;
	        case "-": triggerOp.set(Operator.SUBTRACT); break;
	        case "x": triggerOp.set(Operator.MULTIPLY); break;
	        case "/": triggerOp.set(Operator.DIVIDE);   break;
	      }
	      return triggerOp;
	    }
	  
	  private void makeOperatorerandButton(Button button, final ObjectProperty<Operator> triggerOperator) {
	      button.setStyle("-fx-base: ghostwhite;");
	      button.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent actionEvent) {
	          currentOperator = triggerOperator.get();
	        }
	      });
	    }

	    private Button makeStandardButton(String s) {
	      Button button = new Button(s);
	      button.setStyle("-fx-base: ghostwhite;");
	      buttonMap.put(s, button);
	      button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	      return button;
	    }

	    private void makeNumericButton(final String s, Button button) {
	      button.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent actionEvent) {
	          if (currentOperator == Operator.NOOP) {
	            value.set(value.get() * 10 + Integer.parseInt(s));
	          } else {
	            stackedValue.set(value.get());
	            value.set(Integer.parseInt(s));
	            stackedOperator = currentOperator;
	            currentOperator = Operator.NOOP;
	          }
	        }
	      });
	    }

	    private void makeClearButton(Button button) {
	      button.setStyle("-fx-base: ghostwhite;");
	      button.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent actionEvent) {
	          value.set(0);
	        }
	      });
	    }
	    
	    /**
	     * when equal button being hit we should generate results
	     * @param button is the button to be hit 
	     */
	    private void makeEqualsButton(Button button) {
	      button.setStyle("-fx-base: ghostwhite;");
	      Calculator calculator = new Calculator();
	      button.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent actionEvent) {
	          switch (stackedOperator) {
	          case ADD: value.set(calculator.add(stackedValue.get(), value.get()));
	          break;
	          case MOD: value.set(calculator.mod(stackedValue.get(), value.get()));
	          break;
	          case SUBTRACT: value.set(calculator.subtract(stackedValue.get(), value.get()));
	          break;
	          case DIVIDE: value.set(calculator.divide(stackedValue.get(), value.get()));
	          break;
	          case MULTIPLY: value.set(calculator.multiply(stackedValue.get(), value.get()));
	          break;	          
	          }
	        }
	      });
	    }
}
