package br.com.raspemania.helper;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class ErrorHelper {

    public static void errorLogin(String error, Context context, EditText mEmailField, EditText mPasswordField) {

        String errorCode = error;

        switch (errorCode) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(context, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(context, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(context, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                mEmailField.setError("O endereço de email está mal formatado.");
                mEmailField.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                mPasswordField.setError("Senha incorreta!");
                mPasswordField.requestFocus();
                mPasswordField.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(context, "As credenciais fornecidas não correspondem ao usuário existente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(context, "Efetue login novamente antes de tentar novamente a solicitação de contexto.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(context, "Já existe uma conta com o mesmo endereço de email.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                mEmailField.setError("O endereço de email já está sendo usado por outra conta.");
                mEmailField.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(context, "context credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(context, "A conta do usuário foi desativada por um administrador.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(context, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(context, "Não há registro do usuário correspondente. O usuário pode ter sido excluído.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(context, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(context, "context operation is not allowed. You must enable context service in the console.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                mPasswordField.setError("A senha é inválida, deve conter no mínimo 6 caracteres.");
                mPasswordField.requestFocus();
                break;

            case "GERAL":
                Toast.makeText(context, "Ocorreu um erro inesperado. Favor entrar em contato com raspemania@gmail.com", Toast.LENGTH_LONG).show();
                break;

            case "INVALID_USER":
                Toast.makeText(context, "A conta precisa ser ativada por um administrador para realizar o login!", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_GET_USER":
                Toast.makeText(context, "Erro ao tentar fazer login.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CADASTRO_USER":
                Toast.makeText(context, "Erro ao cadastrar usuário!", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_RECUPERAR_SENHA":
                Toast.makeText(context, "Erro ao recuperar senha!", Toast.LENGTH_LONG).show();
                break;

            default: Toast.makeText(context, "Ocorreu um erro inesperado. Favor entrar em contato com raspemania@gmail.com", Toast.LENGTH_LONG).show();
        }
    }
}
