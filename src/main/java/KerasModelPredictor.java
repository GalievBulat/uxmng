import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Signature;
import org.tensorflow.Tensor;
import org.tensorflow.internal.types.TFloat32Mapper;
import org.tensorflow.ndarray.IntNdArray;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.ndarray.impl.sparse.IntSparseNdArray;
import org.tensorflow.types.TFloat32;
import org.tensorflow.types.TInt32;

public class KerasModelPredictor {

    private static SavedModelBundle model;

    public static synchronized float runPred(int[][] inputValues) {

        System.out.println();
        // Load the saved Keras model in a TensorFlow SavedModelBundle
        if(model == null)
             model = SavedModelBundle.load(
                     "C:\\Users\\Kakad\\IdeaProjects\\occult_color_scheme\\src\\main\\resources\\modelcompl_50it", "serve");

        // Create a TensorFlow Session

        //IntNdArray inputTensor = NdArrays.ofInts(Shape.of(90,160));
        TFloat32 tensor = TFloat32.tensorOf(Shape.of(1,160,90,1));
        for (int i = 0; i < inputValues.length; i++) {
            for (int j = 0; j < inputValues[i].length; j++) {
                //inputTensor.setInt(inputValues[i][j],i,j);
                tensor.setFloat(inputValues[i][j], 0,j,i,0);
            }
        }
        return ((TFloat32)model.function(Signature.DEFAULT_KEY).call(tensor)).getFloat(0,0);
    }
}

