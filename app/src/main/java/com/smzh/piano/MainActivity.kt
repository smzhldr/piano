package com.smzh.piano

import android.Manifest
import android.content.pm.PackageManager
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sin


class MainActivity : AppCompatActivity() {

    private var magicPlayer: MagicPlayer? = null
    private val midiThread = HandlerThread("piano")
    private val midiHandler: Handler
    private lateinit var waveView: WaveView

    init {
        midiThread.start()
        midiHandler = Handler(midiThread.looper)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<RecyclerView>(R.id.rv_piano_panel).run {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = MidiAdapter()
        }
        waveView = findViewById(R.id.wave_view)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }
        magicPlayer = MagicPlayer()
        magicPlayer?.start(BeatsProducer.sampleRate)
    }

    override fun onDestroy() {
        super.onDestroy()
        magicPlayer?.stop()
        magicPlayer = null
        midiThread.quitSafely()
    }


    private fun onMidiDown(index: Int) {
        midiHandler.removeCallbacksAndMessages(null)
        midiHandler.post {
            val data = BeatsProducer.getBeatsData(index)
            magicPlayer?.playData(data, data.size)
            waveView.updateWave(data)
        }
    }

    inner class MidiAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val item =
                LayoutInflater.from(this@MainActivity).inflate(R.layout.midi_item, parent, false)
                    .apply {
                        layoutParams.width = resources.displayMetrics.widthPixels / 15
                    }
            return MidiHolder(item)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as? MidiHolder)?.bind(position)
        }

        override fun getItemCount(): Int {
            return 15
        }

        inner class MidiHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(position: Int) {
                itemView.findViewById<TextView>(R.id.tv_midi_item).run {
                    text = ((position + 2) % 7 + 1).toString()
                    setOnClickListener { onMidiDown(position) }
                }
            }
        }
    }
}