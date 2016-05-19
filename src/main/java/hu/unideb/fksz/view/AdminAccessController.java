package hu.unideb.fksz.view;

/*
 * #%L
 * Traffic-counter
 * %%
 * Copyright (C) 2016 FKSZSoft
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

import hu.unideb.fksz.TrafficCounterLogger;
import hu.unideb.fksz.model.Observation;
import hu.unideb.fksz.model.ObservationDAO;
import hu.unideb.fksz.model.User;
import hu.unideb.fksz.model.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminAccessController implements Initializable {

	@FXML
	private TableView<Observation> adminAccessWindowTableView;
	@FXML
	private TableColumn<Observation, Integer> adminAccessRowIdColumn;
	@FXML
	private TableColumn<Observation, Integer> adminAccessObservationIdColumn;
	@FXML
	private TableColumn<Observation, String> adminAccessVideoTitleColumn;
	@FXML
	private TableColumn<Observation, Integer> adminAccessTrafficCountColumn;
	@FXML
	private TableColumn<Observation, Timestamp> adminAccessDateColumn;
	@FXML
	private TableColumn<Observation, Integer> adminAccessComputerTrafficCountColumn;
	@FXML
	private ListView<String> adminAccessWindowListView;
	@FXML
	private Button adminAccessWindowBackButton;
	ObservableList<Observation> tableData = FXCollections.observableArrayList();

	@FXML
	private void adminAccessWindowBackButtonOnAction() {
		try {
			Stage stage;
			Parent root;
			stage = (Stage) adminAccessWindowBackButton.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TrafficCounterWindow.fxml"));
			root = loader.load();
			loader.<TrafficCounterController> getController().showControls();
			//loader.<TrafficCounterController> getController().resetTitle();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			TrafficCounterLogger.errorMessage(e.toString());
		}
	}

	public void populateUserList() {
		adminAccessWindowListView.setItems(UserDAO.usersString());
		adminAccessWindowListView.getSelectionModel().select(0);
		populateTable();
	}

	private void populateTable() {
		User selectedUser = new User();
		selectedUser.setName(adminAccessWindowListView.getSelectionModel().getSelectedItem());
		populateObservationsTable(selectedUser);
	}

	public void populateObservationsTable(User user) {
		List<Observation> observationsOfUser = ObservationDAO.observationsOf(user);
		tableData = FXCollections.observableList(observationsOfUser);
		adminAccessWindowTableView.setItems(tableData);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adminAccessDateColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Timestamp>("observationDate"));
		adminAccessObservationIdColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("observationId"));
		adminAccessVideoTitleColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, String>("observedVideoTitle"));
		adminAccessTrafficCountColumn
				.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("trafficCount"));
		adminAccessComputerTrafficCountColumn
		.setCellValueFactory(new PropertyValueFactory<Observation, Integer>("computerTrafficCount"));

		adminAccessComputerTrafficCountColumn.setEditable(false);
		adminAccessTrafficCountColumn.setEditable(false);
		adminAccessVideoTitleColumn.setEditable(false);
		adminAccessObservationIdColumn.setEditable(false);
		adminAccessDateColumn.setEditable(false);
	}

	@FXML
	public void onAdminAccessUsersListViewClicked() {
		populateTable();
	}
}
