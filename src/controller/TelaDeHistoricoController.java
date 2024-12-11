package controller;

import view.*;
import model.*;

public class TelaDeHistoricoController extends TelaDeHistoricoView {
    public static String[] preencherHistorico() {
        return TelaDeHistoricoModel.capturarHistorico();
    }
}
