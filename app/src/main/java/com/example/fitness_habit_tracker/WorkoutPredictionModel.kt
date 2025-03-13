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

    fun predictWorkout(accelData: FloatArray): Boolean {
        val input = arrayOf(accelData)
        val output = arrayOf(FloatArray(1))
        interpreter.run(input, output)
        return output[0][0] > 0.5
    }
}
