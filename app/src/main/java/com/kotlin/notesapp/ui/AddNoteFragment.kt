package com.kotlin.notesapp.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.kotlin.notesapp.R
import com.kotlin.notesapp.db.Note
import com.kotlin.notesapp.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch

class AddNoteFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            edt_tittle.setText(note?.tittle)
            edt_note.setText(note?.note)
        }

        btn_save.setOnClickListener { view ->
            val noteTittle = edt_tittle.text.toString().trim()
            val noteBody = edt_note.text.toString().trim()

            if (noteTittle.isEmpty()){
                edt_tittle.error = "tittle required"
                edt_tittle.requestFocus()
                return@setOnClickListener
            }

            if (noteBody.isEmpty()) {
                edt_note.error = "note required"
                edt_note.requestFocus()
                return@setOnClickListener
            }

            launch {

                context?.let {

                    val mNote = Note(noteTittle, noteBody)
                    if(note == null){
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        it.toast("Note Saved")
                    }else {
                        mNote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        it.toast("Note Updated")
                    }


                    val action = AddNoteFragmentDirections.actionSavenote()
                    Navigation.findNavController(view).navigate(action)
                }
            }

//            val action = AddNoteFragmentDirections.actionSavenote()
//            Navigation.findNavController(it).navigate(action)
//    ** setelah membuat launch, pindahkan data diatas ke launch, ganti it dengan view

            }

        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> if(note != null) deleteNote() else context?.toast("Cannot Delete")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Are you sure?")
                setMessage("You cannot undo this operation")
                setPositiveButton("Yes"){ _, _ ->
                    launch {
                        NoteDatabase(context).getNoteDao().deleteNote(note!!)
                        val action = AddNoteFragmentDirections.actionSavenote()
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }

                setNegativeButton("No"){ _, _ ->}
            }.create().show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

//// Async mampu memberi bad experiance pada pengguna

//    private fun saveNote(note: Note){
//        class SaveNote : AsyncTask<Void, Void, Void>(){
//            override fun doInBackground(vararg params: Void?): Void? {
//                NoteDatabase(activity!!).getNoteDao().addNote(note)
//                return null
//            }
//
//            override fun onPostExecute(result: Void?) {
//                super.onPostExecute(result)
//
//                Toast.makeText(activity, "Note Saved", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        SaveNote().execute()
//    }

}