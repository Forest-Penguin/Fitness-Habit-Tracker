package com.example.fitness_habit_tracker

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ActivityClassifier(context: Context) {

    private val interpreter: Interpreter
    private val labelList = listOf("cycling", "running", "stationary", "walking")  // Order from training script

    init {
        interpreter = Interpreter(loadModelFile(context, "activity_model.tflite"))
    }

    private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    fun classify(features: FloatArray): String {
        require(features.size == 7) { "Model expects exactly 7 features." }

        val inputBuffer = ByteBuffer.allocateDirect(4 * 7).order(ByteOrder.nativeOrder())
        for (f in features) inputBuffer.putFloat(f)

        val outputBuffer = ByteBuffer.allocateDirect(4 * labelList.size).order(ByteOrder.nativeOrder())
        outputBuffer.rewind()

        interpreter.run(inputBuffer, outputBuffer)

        outputBuffer.rewind()
        val results = FloatArray(labelList.size)
        outputBuffer.asFloatBuffer().get(results)

        val maxIndex = results.indices.maxByOrNull { results[it] } ?: -1
        return if (maxIndex >= 0) labelList[maxIndex] else "unknown"
    }
}
