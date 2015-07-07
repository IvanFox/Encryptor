
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Ivan on 03/06/2015.
 */
public class EncryptorLogic implements Matrix{


    protected int determinant;
    protected int inverseDeterminant;
    protected int[] encryptionMatrix = new int[MATRIX_SIZE];
    protected int[] invertedMatrix = new int[MATRIX_SIZE];
    protected ArrayList <Integer> convertedText = new ArrayList<Integer>();

    // Проверка задаваемой матрицы на возможность ее использования для шифрования
    // Возвращает булиан
    public boolean checkMatrix(int[] matrix) {
        determinant = matrix[0] * matrix[3] - matrix[2] * matrix[1];
        if(gcd(determinant, NUMBER_OF_LETTERS) == 1){
            encryptionMatrix = matrix;
            findInverse(matrix);
            findInverseDeterminant();
            return true;
        }
        else
            return false;

    }

    // Функция для нахождения инвертированого детирминанта с помощью детерминанта
    // Возвращает интовый результат вычисления
    public int findInverseDeterminant(){
        int temp = determinant;
        int count = 1;
        // If determinant is negative -> convert it to the positive representation
        while (temp < 0){
            temp += NUMBER_OF_LETTERS;
        }

        // find inverse determinant
        while (temp * count % NUMBER_OF_LETTERS != 1){
            count++;
        }
        return inverseDeterminant = count;
    }


    public int gcd(int num1, int num2){
        int largest, smallest, result = 0;
        if (num1 == 0){
            return result;
        }
        if(num1 > num2){
            largest = num1;
            smallest = num2;
        }else {
            largest = num2;
            smallest = num1;
        }
        while (largest % smallest != 0){
            result = largest % smallest;
            largest = smallest;
            smallest = result;
        }
        System.out.println("GCD of: " + num1 + " " + num2 + " is " + result + "\n");
        return Math.abs(result);
    }

    public void findInverse(int[] matrix){
        invertedMatrix[0] = matrix[3];
        invertedMatrix[3] = matrix[0];
        invertedMatrix[1] = -matrix[1];
        invertedMatrix[2] = -matrix[2];
    }

    // Функция предназначенная для конвертации введеного текса в цифровой эквивалент основываясь на массиве
    // букв в интерфейсы Matrix.java
    public int[] analyzeText(String text){
        int convertedText[] = new int[text.length()];
        int letterCounter = 0;

        for (int i = 0; i < text.length(); i++) {
            for (int j = 0; j < NUMBER_OF_LETTERS ; j++) {
                if(text.charAt(i) == ALPHABET[j]){
                    convertedText[letterCounter] = j;
                    letterCounter++;
                }
            }
        }
        return convertedText;
    }

    // Функция для шифрования полученных номеров в буквенный текст
    public String performEncryptDecrypt(int[] number, boolean switcher){
        int i = 0;
        int first, second;
        StringBuilder builder = new StringBuilder();

        int[] chunkForEncDec = new int[2];

        while (i != number.length){
            chunkForEncDec[0] = number[i++];
            chunkForEncDec[1] = number[i++];

            if (!switcher){
                first = findEncDecLetters(invertedMatrix[0], invertedMatrix[1], chunkForEncDec) * inverseDeterminant;
                second = findEncDecLetters(invertedMatrix[2], invertedMatrix[3], chunkForEncDec) * inverseDeterminant;
            }
            else {
                first = findEncDecLetters(encryptionMatrix[0], encryptionMatrix[1], chunkForEncDec);
                second = findEncDecLetters(encryptionMatrix[2], encryptionMatrix[3], chunkForEncDec);
            }
            builder.append(ALPHABET[first % NUMBER_OF_LETTERS]);
            builder.append(ALPHABET[second % NUMBER_OF_LETTERS]);

        }
        System.out.println("\n" + builder + "\n");
        return builder.toString();
    }

    public int findEncDecLetters(int matrix1, int matrix2, int letters[]){
        int returnLetter;
        returnLetter = matrix1 * letters[0] + matrix2 * letters[1];
        while (returnLetter < 0){
            returnLetter += NUMBER_OF_LETTERS;
        }
        return returnLetter;
    }

    public String openChosenFile(String filepath) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        try {
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }

}
