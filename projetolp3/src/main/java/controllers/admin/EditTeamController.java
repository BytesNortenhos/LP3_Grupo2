package controllers.admin;

import Dao.*;
import Models.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditTeamController {
    @FXML
    private ComboBox<String> teamsListDropdown;

    @FXML
    private TextField teamNameText, teamPlayersText, teamMaxPlayersText;

    public void initialize() {
        loadAvailableTeams();
    }
    private void loadAvailableTeams() {
        try {
            TeamDao teamdao = new TeamDao();
            List<List> teams = teamdao.getTeamsNamesAndId();

            List<String> teamNames = new ArrayList<>();
            for (List team : teams) {
                teamNames.add(team.get(1).toString());
            }

            ObservableList<String> teamOptions = FXCollections.observableArrayList(teamNames);
            teamsListDropdown.setItems(teamOptions);
        } catch (SQLException e) {
            showAlert("Database Error", "Erro ao carregar equipes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onTeamSelected() {
        try {
            TeamDao teamdao = new TeamDao();
            String selectedTeamName = teamsListDropdown.getSelectionModel().getSelectedItem();

            if (selectedTeamName != null) {
                List<List> teams = teamdao.getTeamsNamesAndId();
                for (List team : teams) {
                    if (team.get(1).toString().equals(selectedTeamName)) {
                        Team teamSelected = teamdao.getTeamByIdV2(Integer.parseInt(team.get(0).toString()));
                        teamNameText.setText(teamSelected.getName());
                        teamPlayersText.setText(String.valueOf(teamSelected.getMinParticipants()));
                        teamMaxPlayersText.setText(String.valueOf(teamSelected.getMaxParticipants()));
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Erro ao carregar equipes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateTeam() {
        try {
            TeamDao teamdao = new TeamDao();
            String name = teamNameText.getText();


            if (name.length() > 50) {
                showAlert("Erro de Validação", "O nome da equipa não pode ter mais de 50 caracteres!", Alert.AlertType.ERROR);
                return;
            }

            String idTeam = "0";
            int playersCount = Integer.parseInt(teamPlayersText.getText());
            int playersMaxCount = Integer.parseInt(teamMaxPlayersText.getText());

            String selectedTeam = teamsListDropdown.getSelectionModel().getSelectedItem();


            if (selectedTeam == null) {
                showAlert("Erro de Validação", "Selecione uma equipa para atualizar!", Alert.AlertType.ERROR);
                return;
            }

            // Buscar o ID da equipa selecionada
            List<List> teams = teamdao.getTeamsNamesAndId();
            for (List team : teams) {
                if (team.get(1).toString().equals(selectedTeam)) {
                    idTeam = team.get(0).toString();
                }
            }


            TeamDao.updateTeams(idTeam, name, playersCount, playersMaxCount);


            showAlert("Sucesso", "Equipa atualizada com sucesso!", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Erro de Validação", "Formato inválido para o número de jogadores!", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erro na BD", "Erro ao atualizar a equipa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
