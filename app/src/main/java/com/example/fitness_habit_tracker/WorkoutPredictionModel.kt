import android.content.Context
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import org.tensorflow.lite.Interpreter

class WorkoutPredictionModel(private val context: Context) {

    private val interpreter: Interpreter

    init {
        val modelFile = loadModelFile("workout_model.tflite")
        interpreter = Interpreter(modelFile)
    }

    private fun loadModelFile(modelName: String): ByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predictWorkout(accelData: FloatArray): String {
        val input = arrayOf(accelData)
        val output = Array(1) { FloatArray(3) }  // 3 classes
        interpreter.run(input, output)

        val classes = listOf("WALKING", "RUNNING", "BIKING")
        val predictedIndex = output[0].indices.maxByOrNull { output[0][it] } ?: 0
        return classes[predictedIndex]
    }

}
