package com.jchc.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.jchc.videogamesdb.R
import com.jchc.videogamesdb.application.VideoGamesDBApp
import com.jchc.videogamesdb.data.GameRepository
import com.jchc.videogamesdb.data.db.model.GameEntity
import com.jchc.videogamesdb.databinding.GameDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException

class GameDialog(
    private val newGame: Boolean = true,
    private var game:GameEntity = GameEntity(
        title = "",
        genre = "",
        developer = ""
    ),
    private val updateUI: () -> Unit,
    private val message:(String) -> Unit
): DialogFragment() {

    private var _binding: GameDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null
    private lateinit var repository: GameRepository


    //Aquí se crea y configura el diálogo de forma inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)
        builder = AlertDialog.Builder(requireContext())
        repository= (requireContext().applicationContext as VideoGamesDBApp).repository

        binding.apply{
            binding.tietTitle.setText(game.title)
            binding.tietGenre.setText(game.genre)
            binding.tietDeveloper.setText(game.developer)
        }

        dialog = if(newGame)
            buildDialog(getString(R.string.save), getString(R.string.cancel), {
                //Accion de guardar
                //Click del botón positivo
                game.apply{
                    title= binding.tietTitle.text.toString()
                    genre =binding.tietGenre.text.toString()
                    developer = binding.tietDeveloper.text.toString()
                }
                try{
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }
                    message(getString(R.string.savedGame))
                    updateUI()
                }catch (e: IOException) {
                    e.printStackTrace()
                    message(getString(R.string.errorsavedGame))
                }
            }, {
                //Acción de cancelar
            })
        else
            buildDialog(getString(R.string.update), getString(R.string.remove), {
                //acción de actualizar
                game.apply{
                    title= binding.tietTitle.text.toString()
                    genre =binding.tietGenre.text.toString()
                    developer = binding.tietDeveloper.text.toString()
                }
                try{
                    lifecycleScope.launch {
                        repository.updateGame(game)
                    }
                    message(getString(R.string.updatedGame))
                    updateUI()
                }catch (e: IOException) {
                    e.printStackTrace()
                    message(getString(R.string.errorupdatedGame))
                }
            }, {
                //acción de borrar
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirm))
                    .setMessage("¿Realmente quieres borrar el juego ${game.title}?")
                    .setPositiveButton("Aceptar") { _, _ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteGame(game)
                            }
                            message(getString(R.string.removedGame))
                            updateUI()
                        } catch (e: IOException) {
                            e.printStackTrace()
                            message(getString(R.string.errorremovedGame))
                        }
                    }
                .setNegativeButton(getString(R.string.cancel)){dialog, _ ->
                    dialog.dismiss()
                }
                    .create()
                    .show()
            })
        /*dialog = builder.setView(binding.root)
            .setTitle("Juego")
            .setPositiveButton(getString(R.string.Guardar), DialogInterface.OnClickListener { _, _ ->
                //Click del botón positivo
                game.apply{
                    title= binding.tietTitle.text.toString()
                    genre =binding.tietGenre.text.toString()
                    developer = binding.tietDeveloper.text.toString()
                }
                try{
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.savedGame),
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI()
                }catch (e: IOException){
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.errorsavedGame),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            .setNegativeButton(getString(R.string.Cancelar)){_, _ ->
                //Click del botón negativo
            }
            .create() */

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se va a llamar después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()
        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietTitle.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietGenre.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietDeveloper.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
    }

    private fun validateFields(): Boolean =
        (binding.tietTitle.text.toString().isNotEmpty() &&
                binding.tietGenre.text.toString().isNotEmpty() &&
                binding.tietDeveloper.text.toString().isNotEmpty())

    private fun buildDialog(
        btn1Text: String,
        btn2Text:String,
        positiveButton:()->Unit,
        negativeButton:()->Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.game))
            .setPositiveButton(btn1Text){_, _ ->
                //Acción para botón positivo
                positiveButton()
            }
            .setNegativeButton(btn2Text){_,_ ->
                //Acción para botón negativo
                negativeButton()
            }
            .create()

}