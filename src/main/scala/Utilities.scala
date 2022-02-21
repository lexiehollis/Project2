import scala.io.StdIn.readLine
import org.apache.spark.sql.{SparkSession, functions}
import java.io.{File, PrintWriter, FileOutputStream}
import scala.Console.println
import P2._

object Utilities {
  var admin: Boolean = false
  def junk(): Unit = {
    spark.sql(
      "set hive.exec.dynamic.partition.mode=nonstrict"
    )
    spark.sql("DROP TABLE IF EXISTS userpass")
    spark.sql(
      "CREATE TABLE IF NOT EXISTS userpass (user STRING, pass STRING, admin STRING) "
        + "ROW FORMAT DELIMITED FIELDS TERMINATED BY ','"
    )
    spark.sql(
      "LOAD DATA LOCAL INPATH 'input/userpass.txt' OVERWRITE INTO TABLE userpass"
    )

//    spark.sql("CREATE TABLE IF NOT EXISTS branch_a (bev STRING, branch STRING)" +
//        "ROW FORMAT DELIMITED FIELDS TERMINATED BY ','")
//    spark.sql("CREATE TABLE IF NOT EXISTS Partitioned_abc(bev STRING) PARTITIONED BY (branches STRING)")

//    spark.sql("LOAD DATA LOCAL INPATH 'input/Bev_BranchA.txt' OVERWRITE INTO TABLE branch_a")
//    spark.sql("INSERT OVERWRITE TABLE Partitioned_abc PARTITION(branches) SELECT bev,branch FROM all_branch")
  }

  def end(): Unit = {
    println("Thank you for your time, hope you enjoyed those queries!")
  }

  def chooseN(n: Byte): Byte = {
    // val temp = readLine()
    var input: Char = readLine().charAt(0)
    var inByte: Byte = 0
    var goodIn: Boolean = false

    n match {
      case 1 =>
        println(
          "Sorry, but you have to choose '1'... Huh... almost feels like you have no choice at all... OK you can go now. "
        );
        goodIn = true;
        inByte = 1.toByte
      case 2 =>
        while (!goodIn) {
          input match {
            case '1' => goodIn = true; inByte = 1.toByte
            case '2' => goodIn = true; inByte = 2.toByte
            case _ =>
              println("Sorry, but you have to choose '1', or '2': ");
              input = readChar()
          }
        }
      case 3 =>
        while (!goodIn) {
          input match {
            case '1' => goodIn = true; inByte = 1.toByte
            case '2' => goodIn = true; inByte = 2.toByte
            case '3' => goodIn = true; inByte = 3.toByte
            case _ =>
              println("Sorry, but you have to choose '1', '2', or '3': ");
              input = readChar()
          }
        }
      case 4 =>
        while (!goodIn) {
          input match {
            case '1' => goodIn = true; inByte = 1.toByte
            case '2' => goodIn = true; inByte = 2.toByte
            case '3' => goodIn = true; inByte = 3.toByte
            case '4' => goodIn = true; inByte = 4.toByte
            case _ =>
              println("Sorry, but you have to choose '1', '2', '3', or '4': ");
              input = readChar()
          }
        }
      case 5 =>
        while (!goodIn) {
          input match {
            case '1' => goodIn = true; inByte = 1.toByte
            case '2' => goodIn = true; inByte = 2.toByte
            case '3' => goodIn = true; inByte = 3.toByte
            case '4' => goodIn = true; inByte = 4.toByte
            case '5' => goodIn = true; inByte = 5.toByte
            case _ =>
              println(
                "Sorry, but you have to choose '1', '2', '3', '4', or '5': "
              ); input = readChar()
          }
        }
      case 6 =>
        while (!goodIn) {
          input match {
            case '1' => goodIn = true; inByte = 1.toByte
            case '2' => goodIn = true; inByte = 2.toByte
            case '3' => goodIn = true; inByte = 3.toByte
            case '4' => goodIn = true; inByte = 4.toByte
            case '5' => goodIn = true; inByte = 5.toByte
            case '6' => goodIn = true; inByte = 6.toByte
            case _ =>
              println(
                "Sorry, but you have to choose '1', '2', '3', '4', '5', or '6': "
              ); input = readChar()
          }
        }
      case 7 =>
        while (!goodIn) {
          input match {
            case '1' => goodIn = true; inByte = 1.toByte
            case '2' => goodIn = true; inByte = 2.toByte
            case '3' => goodIn = true; inByte = 3.toByte
            case '4' => goodIn = true; inByte = 4.toByte
            case '5' => goodIn = true; inByte = 5.toByte
            case '6' => goodIn = true; inByte = 6.toByte
            case '7' => goodIn = true; inByte = 7.toByte
            case _ =>
              println(
                "Sorry, but you have to choose '1', '2', '3', '4', '5', '6', or '7': "
              ); input = readChar()
          }
        }
    }
    inByte
  }

  def menuLev2(options: List[String]): Unit = {
    var continue = true
    while (continue) {
      getOption(options) match {
        case "E"       => q1 // rural
        case "F"       => q2 // urban
        case "Unknown" => q3 // suburban?
        case "PEDAL"   => q4 // Cyclist
        case b         => continue = false
      }
    }
  }

  def signUp(): String = {
    var continue = true
    var user = ""
    while (continue) {
      println("Please enter a Username:")
      user = readLine().trim()
      if (userExists(user)) println("That username is taken, try again:")
      else continue = false
    }
    println("Please enter a Password:")
    val pass = readLine()

    val pw = new PrintWriter(
      new FileOutputStream(
        new File("input/userpass.txt"),
        true /* append = true */
      )
    )
    pw.append(s"$user,$pass,false\n")
    pw.close()
    junk()
    user
  }

  def logIn(user: String): Unit = {
    val q = spark.sql(
      s"SELECT admin FROM userpass WHERE user = '$user' AND admin = 'true'"
    )
    if (q.count() == 0) admin = false else admin = true
  }

  def userExists(user: String): Boolean = {
    val q = spark.sql(s"SELECT * FROM userpass WHERE user = '$user'")
    if (q.count() == 0) false else true
  }

  def authPass(user: String, pass: String): Boolean = {
    val q = spark.sql(
      s"SELECT * FROM userpass WHERE user = '$user' AND pass = '$pass'"
    )
    if (q.count() == 0) false else true
  }
  def getOption(l: List[String]): String = {
    val menu = new MyMenu(l)
    menu.printMenu()
    val in = chooseN(l.length.toByte)
    menu.selectOption(in)
  }
}
