/**
 * Ce logiciel est distribué à des fins éducatives.
 *
 * Il est fourni "tel quel", sans garantie d’aucune sorte, explicite
 * ou implicite, notamment sans garantie de qualité marchande, d’adéquation
 * à un usage particulier et d’absence de contrefaçon.
 * En aucun cas, les auteurs ou titulaires du droit d’auteur ne seront
 * responsables de tout dommage, réclamation ou autre responsabilité, que ce
 * soit dans le cadre d’un contrat, d’un délit ou autre, en provenance de,
 * consécutif à ou en relation avec le logiciel ou son utilisation, ou avec
 * d’autres éléments du logiciel.
 *
 * (c) 2022-2023 Romain Wallon - Université d'Artois.
 * Tous droits réservés.
 */

package fr.univartois.butinfo.ihm.taquin.controller;

import java.net.URL;

import fr.univartois.butinfo.ihm.taquin.model.Grid;
import fr.univartois.butinfo.ihm.taquin.model.ITaquinController;
import fr.univartois.butinfo.ihm.taquin.model.Taquin;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * La classe TaquinController propose un contrôleur permettant de gérer un jeu du Taquin
 * présenté à l'utilisateur sous la forme d'une interface graphique JavaFX.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class TaquinController implements ITaquinController {

    /**
     * Le label affichant le nombre de déplacements réalisés par l'utilisateur.
     */
    @FXML
    private Label nbMoves;

    /**
     * La grille affichant les boutons permettant de jouer au Taquin.
     */
    @FXML
    private GridPane gridPane;

    /**
     * Les boutons représentant les tuiles du jeu du Taquin.
     */
    private Button[][] buttons;

    /**
     * Le modèle du Taquin avec lequel ce contrôleur interagit.
     */
    private Taquin taquin;

    /**
     * Stocke la Scene sur laquelle le jeu du Taquin est affiché.
     *
     * @param scene La Scene sur laquelle le jeu est affiché.
     */
    public void setScene(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case UP -> taquin.pushUp();
                case LEFT -> taquin.pushLeft();
                case DOWN -> taquin.pushDown();
                default -> taquin.pushRight();
            }
            e.consume();
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.ihm.taquin.model.ITaquinController#setModel(fr.univartois.
     * butinfo.ihm.taquin.model.Taquin)
     */
    @Override
    public void setModel(Taquin taquin) {
        this.taquin = taquin;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.ihm.taquin.model.ITaquinController#initNbMoves(javafx.beans.
     * property.IntegerProperty)
     */
    @Override
    public void initNbMoves(IntegerProperty property) {
        this.nbMoves.textProperty().bind(property.asString());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.butinfo.ihm.taquin.model.ITaquinController#initGrid(fr.univartois.
     * butinfo.ihm.taquin.model.Grid)
     */
    @Override
    public void initGrid(Grid grid) {
        buttons = new Button[grid.size()][grid.size()];

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.size(); j++) {
                buttons[i][j] = createButton(i, j, grid.get(i, j).getProperty());
                gridPane.add(buttons[i][j], j, i);
            }
        }
    }

    /**
     * Crée le bouton devant apparaître à la position donnée.
     *
     * @param row La ligne du bouton.
     * @param column La colonne du bouton.
     * @param property La propriété qui doit être affichée par le bouton.
     *
     * @return Le bouton qui a été créé.
     */


    private Button createButton(int row, int column, IntegerProperty property) {
        // Crée le bouton, dans les bonnes dimensions.
        Button button = new Button();
        button.setTextFill(Color.WHITE);
        button.setFont(new Font(24));
        button.setPrefWidth(100);
        button.setPrefHeight(100);

        // Ajoute l'action à réaliser lorsque l'utilisateur clique sur le bouton.
        button.setOnAction(e -> taquin.push(row, column));

        // Lie l'affichage du bouton à la propriété qu'il doit afficher.
        button.textProperty().bind(property.asString());
        button.visibleProperty().bind(property.isNotEqualTo(0));

        // Configure un arrière-plan en lien avec le nombre affiché.
        button.setBackground(createBackground(property.get()));
        property.addListener((p, o, n) -> button.setBackground(createBackground(n)));

        return button;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.ihm.taquin.model.ITaquinController#startGame()
     */
    @Override
    public void startGame() {
        int size = buttons.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setDisable(false);
            }
        }
    }

    /**
     * Redémarre le jeu affiché sur la vue.
     */
    @FXML
    public void restart() {
        taquin.restartGame();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.ihm.taquin.model.ITaquinController#endGame()
     */
    @Override
    public void endGame() {
        int size = buttons.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setDisable(true);
            }
        }
    }

    /**
     * Détermine l'arrière-plan de la tuile ayant l'indice donné.
     *
     * @param index L'indice de la tuile.
     *
     * @return L'arrière-plan associé à la tuile.
     */
    private Background createBackground(Number index) {
        URL urlImage = getClass().getResource("../view/images/iut-" + index + ".jpg");
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(urlImage.toExternalForm(), 100, 100, true, false),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        return new Background(backgroundImage);
    }

}
