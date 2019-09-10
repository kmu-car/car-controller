package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CarsController {
//	@FXML
//	private Button upButton;
//	@FXML
//	private Button downButton;
	@FXML
	private Button leftButton;
	@FXML
	private Button rightButton;
	@FXML
	private Label action;
	@FXML
	private VBox vBoxScore;
	@FXML
	private ScrollPane scroll;
	int index = 0;
	@FXML
	private Button BtnAddCoordinate;
	@FXML
	private ListView<String> listBoxMain;
	@FXML
	private TextField txtAddItem;
	@FXML
	private Button BtnBreak;
	@FXML
	private Button BtnAccelerate;
	@FXML
	private Button BtnStop;
	
	
	@FXML
	private ComboBox<String> comboBox;
	
	final ObservableList<String> listItems = FXCollections.observableArrayList("Start Drive!");
	DataInputStream inputStream ;
	DataOutputStream outputStream;
	
    public static int remoteServerPort = 3001;
	public static String remoteServerAddress = "10.223.113.197"; // ip address of server
	public double speedUpdate;
	public final double ACCELERATE = 2.2;
	public final double BREAK = -2.2;
	public final double STOP = 0.0;
    
	@FXML
	public void initialize() {
		connect();
//		kuy();
//		simulator();
		listBoxMain.setItems(listItems);

		// Set disable buttons to start
		BtnAddCoordinate.setDisable(false);
		leftButton.setDisable(false);
		rightButton.setDisable(false);
//		
		// Add a ChangeListener to TextField to look for change in focus
		txtAddItem.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (txtAddItem.isFocused()) {
					BtnAddCoordinate.setDisable(false);
					leftButton.setDisable(false);
					rightButton.setDisable(false);
				}
			}
		});
		comboBox.getItems().removeAll(comboBox.getItems());
		comboBox.getItems().addAll("Option A", "Option B", "Option C");
		comboBox.getSelectionModel().select("Option B");
		
		
		
	}
	
	public void connect() {
		try {
			InetAddress remoteServerInetAddress = InetAddress.getByName(remoteServerAddress);
			Socket socket = new Socket(remoteServerInetAddress, remoteServerPort);
			
			System.out.println("Connect to socket server: " + socket.getRemoteSocketAddress());

			// pointer to stream which can receive data from server
			inputStream = new DataInputStream(socket.getInputStream());

			// pointer to stream which can send data to server
			outputStream = new DataOutputStream(socket.getOutputStream());
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(String n) {
		try {
			System.out.print("Type any command: ");
			String message = null;
			String response = null;
			
				message= n;
				System.out.println("Wrting command on the server");
				
				// data to send to server
				outputStream.writeUTF(message);
				outputStream.flush();
				
				// data received from server
				response = inputStream.readUTF();
				System.out.println("Command send back by the server: " + response);
				
//				socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void simulator() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CarSimulatorController.fxml"));
			Stage stage = new Stage();
			stage.setScene(new Scene((Parent) loader.load()));
			stage.show();
		} catch(Exception e) {
		}
	}
	
	// get speed from simulator
	private double getSpeed() {
		return speedUpdate;
	}
	
	private void setSpeed(Double speed) {
		speedUpdate = speed;
	}

	@FXML
	private void accelerateSpeed(ActionEvent action) {
		speedUpdate = getSpeed() + ACCELERATE;
		send(speedUpdate+"");
	}
	
	@FXML
	private void breakCar(ActionEvent action) {
		speedUpdate = getSpeed() + BREAK;
		send(speedUpdate+"");
	}
	
	@FXML
	private void stopCar() {
		speedUpdate = 0;
		send(speedUpdate+"");
	}
	
	@FXML
	private void showCoordinate(ActionEvent action) {
		listItems.add(txtAddItem.getText());
		send(txtAddItem.getText());
		System.out.println(txtAddItem.getText());
	}

	@FXML
	private void showLeft(ActionEvent action) {
		listItems.add("Left");
		send("Left");
		System.out.println("Left");
	}
	
	@FXML
	private void showRight(ActionEvent action) {
		listItems.add("Right");
		send("Right");
		System.out.println("Right");
		
	}
		
	
//	@FXML
//	private void showUp(ActionEvent action) {
//		listItems.add("Up");
//		send("Up");
//		System.out.println("Up");
//	}
//	
//	@FXML
//	private void showDown(ActionEvent action) {
//		listItems.add("Down");
//		send("Down");
//		System.out.println("Down");
//	}
//	
	
}
