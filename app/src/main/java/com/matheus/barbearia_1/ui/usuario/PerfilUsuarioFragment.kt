package com.matheus.barbearia_1.ui.usuario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.matheus.barbearia_1.MainActivity
import com.matheus.barbearia_1.R
import com.matheus.barbearia_1.databinding.FragmentPerfilUsuarioBinding


class PerfilUsuarioFragment : Fragment() {

    private var _binding: FragmentPerfilUsuarioBinding? = null

    private lateinit var userProfileImageView: ImageView
    private lateinit var registerNameEditText: EditText
    private lateinit var registerEmailEditText: EditText
    private lateinit var registerPasswordEditText: EditText
    private lateinit var registerConfirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var sairButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_perfil_usuario, container, false)

        // Inicializa o Firebase Auth
        auth = FirebaseAuth.getInstance()

        userProfileImageView = view.findViewById(R.id.userProfileImageView)
        registerNameEditText = view.findViewById(R.id.registerNameEditText)
        registerEmailEditText = view.findViewById(R.id.registerEmailEditText)
        registerPasswordEditText = view.findViewById(R.id.registerPasswordEditText)
        registerConfirmPasswordEditText = view.findViewById(R.id.registerConfirmPasswordEditText)
        registerButton = view.findViewById(R.id.registerButton)
        sairButton = view.findViewById(R.id.sairButton)

        // Acessar currentUser
        val user = MainActivity.usuarioLogado

        if (user != null) {
            // Se o usuário está logado, verifica o provedor
            val isGoogleUser = isGoogleLogin(user)
            registerEmailEditText.isEnabled = false
            registerEmailEditText.isFocusable = false
            registerEmailEditText.isClickable = false

            // Desabilita os campos de senha caso seja um usuário do Google
            if (isGoogleUser) {
                registerPasswordEditText.isEnabled = false // A senha não pode ser alterada
                registerConfirmPasswordEditText.isEnabled = false // A confirmação de senha também não
            } else {
                registerPasswordEditText.isEnabled = true // Permite alterar a senha para usuários de e-mail/senha
                registerConfirmPasswordEditText.isEnabled = true // Permite alterar a confirmação de senha
            }

            // Exibe a foto do perfil usando a biblioteca Glide
            Glide.with(this).load(user.photoUrl).into(userProfileImageView)

            // Exibe o botão de logout
            sairButton.visibility = View.VISIBLE
        }

        registerButton.setOnClickListener {
            updateUser()
        }

        sairButton.setOnClickListener {
            signOut()
        }

        return view
    }

    private fun signOut() {
        auth.signOut()
        MainActivity.usuarioLogado = null
        Toast.makeText(
            context,
            "Logout realizado com sucesso!",
            Toast.LENGTH_SHORT
        ).show()
        MainActivity.usuarioLogado = null

        requireActivity().finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Exibe os dados do usuário logado, se disponível
        val user = MainActivity.usuarioLogado
        var user_firebase = MainActivity.usuarioLogado
        if (user_firebase != null) {
            registerNameEditText.setText(user_firebase.displayName)
            registerEmailEditText.setText(user_firebase.email)
        }
    }

    private fun updateUser() {
        val name = registerNameEditText.text.toString().trim()
        val password = registerPasswordEditText.text.toString().trim()
        val confirmPassword = registerConfirmPasswordEditText.text.toString().trim()

        // Acessar currentUser
        val user = MainActivity.usuarioLogado

        if (user != null) {
            // Se o usuário está logado, atualiza os dados
            if (password.isNotEmpty() && password == confirmPassword) {
                // Se a senha foi alterada, atualiza a senha
                updatePassword(user, password)
            }
            updateProfile(user, name)
        } else {
            Toast.makeText(context, "Não foi possível encontrar o usuário logado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProfile(user: FirebaseUser?, displayName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Nome do usuário alterado com sucesso.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Não foi possível alterar o nome do usuário.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updatePassword(user: FirebaseUser, newPassword: String) {
        user.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Senha alterada com sucesso.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Erro ao alterar a senha.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun isGoogleLogin(user: FirebaseUser): Boolean {
        // Verifica se o provedor de autenticação do usuário é o Google
        return user.providerData.any { it.providerId == "google.com" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
