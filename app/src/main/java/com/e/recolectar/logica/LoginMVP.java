package com.e.recolectar.logica;

public interface LoginMVP {
    interface Vista {
        void onEmailValidationError(String errorType);

        void onPassValidationError(String errorType);

        void onError(String error);
    }

    interface Presentacion {
        void doLoginWhitEmailPassword(String email, String password);

        boolean validarEmailPassword(String email, String password);

        void onEmailValidationError(String errorType);

        void onPassValidationError(String errorType);

        void onLoginError(String error);
    }


}
