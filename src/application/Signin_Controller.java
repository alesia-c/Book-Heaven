package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Signin_Controller {

	@FXML
	private Button btn_goBack;
	@FXML
	private Button btn_signin;

	@FXML
	private ComboBox<String> cmb_city;

	@FXML
	private Hyperlink link_login;

	@FXML
	private PasswordField psw_password;

	@FXML
	private TextField txt_email;
	@FXML
	private TextField txt_phone;
	@FXML
	private TextField txt_username;

	Helper helper = new Helper();

	@FXML
	void initialize() {

		ObservableList<String> qarqet = FXCollections.observableArrayList("Tirane", "Durres", "Elbasan", "Shkoder", "Vlore", "Fier", "Korce", "Lezhe", "Diber", "Berat", "Kukes", "Gjirokaster");
		cmb_city.setItems(qarqet);


		btn_signin.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			btn_signin.setStyle("-fx-background-color: #345e61; -fx-font-family: Helvetica; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		});
		btn_signin.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			btn_signin.setStyle("-fx-background-color: #1d4c4f; -fx-font-family: Helvetica; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
		});

	}

	@FXML
	public void create_Account(ActionEvent event) {

		String Username = txt_username.getText().trim();
		String Password = psw_password.getText().trim();
		String Email = txt_email.getText();
		String Phone = txt_phone.getText();
		String Region = cmb_city.getValue();

		String errors = "";//Ky string do shfaqet si error alert nese te dhenat nuk jepen te sakta nga perdoruesi.

		//Variabel e cila do numeroje sa fusha te dhenash jane futur sakte.
		//Nese numri i saj do jete i barabarte me 5(sa nr i te dhenave qe jep perodruesi), atehere thirret metoda register.
		int isCorrectData = 0;

		if (Username.length() > 0) {
			isCorrectData++;
		} else {
			errors += "The username cannot be left blank.\n";
		}


		if (Password.length() > 8) {
			isCorrectData++;
		} else {
			errors += "Your password has to be at least 8 characters long.\n";
		}

		//bllok qe kontrollon nese emaili eshte i sakte
		Pattern pattern = Pattern.compile("^.+@.+\\..+$");
		Matcher matcher = pattern.matcher(Email);
		if (matcher.matches()) {
			isCorrectData++;
		} else {
			errors += "The email you provided is not valid.\n";
		}


		//bllok qe kontrolon nese numri ka vetem karaktere numerike dhe 10 shifra
		String regexStr = "^[0-9]{10}$";
		if (Phone.matches(regexStr)) {
			isCorrectData++;
		} else {
			errors += "Your number cannot contain alphabetic characters, or be less than 10 digits long.\n";
		}


		if (Region != null) {
			isCorrectData++;
		} else {
			errors += "Please choose the region you live in.\n";
		}


		if (isCorrectData == 5) {
			register(Username, Password, Email, Phone, Region);
			helper.changeScene("Home.fxml");

		}
		else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Account could not be created because: .");
			alert.setContentText(errors + "Read these tips and try again!");
			alert.show();
		}
	}


	//Metode ndihmese qe thirret kur te gjitha te dhenat e futura nga perdoruesi jane te rregullta.
	//Kjo metode kontrollon nqs ka perdorues me te njejtin username dhe nese nuk ka atehere ben regjistrimin ne database
	private void register(String username, String password, String email, String phone, String region) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement checkUserExists = null;
		ResultSet resultSet = null;


		try {

			connection  = DBConnection.getConnection();

			checkUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
			checkUserExists.setString(1, username);

			resultSet = checkUserExists.executeQuery();


			//kontrollon nese username-i eshte perdorur
			if (resultSet.isBeforeFirst()) {
				Alert alert =new Alert(AlertType.ERROR);
				alert.setContentText("This username is already taken. Please, choose another one.");
				alert.show();
			}
			else {
				preparedStatement = connection.prepareStatement("insert into users " 
						+ "(username, password, email, phone_no, municipality) " 
						+ "values (?, ?, ?, ?, ?)");


				preparedStatement.setString(1, username);

				preparedStatement.setString(2,password);

				preparedStatement.setString(3, email);

				preparedStatement.setString(4, phone);

				preparedStatement.setString(5, region);

				boolean result = preparedStatement.execute();

				Alert alert = new Alert(AlertType.INFORMATION);
				if (result != true) {
					Main.username = username;

					alert.setTitle("Success!");
					alert.setContentText("Your account was created successfully.");
					alert.show();
				} else {
					alert.setTitle("ERROR!");
					alert.setContentText("There was an error. Try again!");
					alert.show();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (checkUserExists != null) {
				try {
					checkUserExists.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}


	@FXML
	void goBack(ActionEvent event) {

		helper.changeScene("Home.fxml");

	}


	@FXML
	void goto_login(ActionEvent event) {

		helper.changeScene("Log_In.fxml");

	}


}
