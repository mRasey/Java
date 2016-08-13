import java.io.*

fun main(args: Array<String>) {
    var file = File("F:/3.txt")
    var outFile = File("F:/2.txt")
    var fileReader = FileReader(file)
    var fileWrite = FileWriter(outFile)
    var bfr = BufferedReader(fileReader)
    var bfw = BufferedWriter(fileWrite)
    var line = bfr.readLine()
    var names = line.split(" ")
    for(name in names) {
        bfw.write(name)
        bfw.newLine()
    }
    println(line.length)
}
