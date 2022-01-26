package com.romes.voicerecorderapp

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.romes.voicerecorderapp.databinding.ListLayoutBinding
import com.romes.voicerecorderapp.databinding.RecordLayoutBinding
import kotlinx.coroutines.Runnable
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment(),onItemListClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : ListLayoutBinding
    private lateinit var allFiles : Array<File>
    var isPlaying: Boolean = false
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var fileToPlay : File
    private lateinit var seekBarHandler : Handler
    private lateinit var updateSeekbar : Runnable
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListLayoutBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var path:String = context?.getExternalFilesDir(null)!!.absolutePath
        val directory : File = File(path)
        allFiles = directory.listFiles()
        binding.recorderList.layoutManager = LinearLayoutManager(context)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playerSheet.playerSheet)
        binding.recorderList.adapter = context?.let { VoiceListAdapter(allFiles, it,this) }
        binding.playerSheet.playButton.setOnClickListener {
            if (isPlaying) {
                pauseAudio()


            }
            else{
                resumeAudio()

            }
        }
        binding.playerSheet.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                pauseAudio()

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                var progress : Int = p0!!.progress
                mediaPlayer?.seekTo(progress)
                resumeAudio()

            }

        })






    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClick(file: File, position: Int) {
        Log.d("Play Log","File Playing" + file.name)
        fileToPlay = file
        if(isPlaying){
            stopAudio()
            isPlaying = false
            playAudio(fileToPlay)
        }
        else{
            playAudio(fileToPlay)

        }

    }

    private fun stopAudio() {

        isPlaying = false
        binding.playerSheet.playButton.setImageDrawable(context?.resources?.getDrawable(R.drawable.playbutton,null))
        binding.playerSheet.playerStatus.text = "Stopped Playing"
        mediaPlayer?.stop()
        seekBarHandler.removeCallbacks(updateSeekbar)


    }

    private fun playAudio(fileToPlay : File) {
        isPlaying = true
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                    AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            )
            setDataSource(fileToPlay.absolutePath)
            prepare()
            start()
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.playerSheet.playButton.setImageDrawable(context?.resources?.getDrawable(R.drawable.player_stop_button, null))
        binding.playerSheet.playerFilename.text = fileToPlay.name
        binding.playerSheet.playerStatus.text = "Playing"
        mediaPlayer?.setOnCompletionListener() {
            stopAudio()
            binding.playerSheet.playerStatus.text = "Finished"
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
        binding.playerSheet.seekBar.max = mediaPlayer!!.duration
        seekBarHandler = Handler()
        updateRunnable()
        seekBarHandler.postDelayed(updateSeekbar,0)

    }

    private fun updateRunnable() {
        updateSeekbar = Runnable {
            binding.playerSheet.seekBar.progress = mediaPlayer!!.currentPosition
            seekBarHandler.postDelayed(updateSeekbar,500)
        }

    }

    private fun pauseAudio(){
        mediaPlayer?.pause()
        binding.playerSheet.playButton.setImageDrawable(context?.resources?.getDrawable(R.drawable.playbutton,null))
        binding.playerSheet.playerStatus.text = "Paused"
        seekBarHandler.removeCallbacks(updateSeekbar)
        isPlaying = false

    }

    private fun resumeAudio(){
        mediaPlayer?.start()
        binding.playerSheet.playButton.setImageDrawable(context?.resources?.getDrawable(R.drawable.player_stop_button, null))
        binding.playerSheet.playerStatus.text = "Playing"
        isPlaying = true
        updateRunnable()
        seekBarHandler.postDelayed(updateSeekbar,0)


    }











    }
