package de.tucottbus.kt.uasr_data_coins;

import gui.decodeMessage;
import gui.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import de.tucottbus.kt.dlabpro.recognizer.Recognizer;

/**
 * Basic wrapper of dLabPro {@link Recognizer recognizer} for live demonstration
 * of the UASR acoustic coin recognition experiment. The wrapper uses {@link 
 * $UASR_HOME-data/coins/LIVE/info/recognizer.cfg} as configuration file.
 *
 * <h3>Data Preparation</h3>
 * <p>First you need to create acoustic models using UASR's <a href=
 * "https://rawgit.com/matthias-wolff/UASR/master/manual/automatic/HMM.xtp.html"
 * ><code>HMM.xtp</code></a> tool:</p>
 * <pre>
 *   dlabpro $UASR_HOME/scripts/dlabpro/HMM.xtp trn $UASR_HOME-data/coins/LIVE/info/HMM-trn.cfg</pre>
 * 
 * <p>After training, acoustic models and grammar need to be compiled and packed 
 * into binary data files required by the coin recognizer with the <a href= 
 * "https://rawgit.com/matthias-wolff/UASR/master/manual/automatic/tools/REC_PACKDATA.xtp.html"
 * ><code>REC_PACKDATA.xtp</code></a> tool:</p>
 * <pre>
 *  dlabpro $UASR_HOME/scripts/dlabpro/tools/REC_PACKDATA.xtp rec $UASR_HOME-data/coins/LIVE/info/REC_PACKDATA.cfg</pre>
 * <p>The output of the packaging tool goes into {@code $UASR_HOME-data/coins/LIVE/model}.
 * There you also find renderings of the grammar ({@code GRM.svg}) and the
 * lexicon ({@code lexicon.svg}).</p>
 * 
 * @author Matthias Wolff
 */
public class CoinsRecognizer extends Recognizer
{
  private CoinsRecognizer(File exeFile, Properties config)
  throws FileNotFoundException, IllegalArgumentException
  {
    super(exeFile, config);
  }
  
  /**
   * Creates a new coin recognizer.
   * 
   * @return
   *   The coin recognizer instance.
   * @throws FileNotFoundException
   *           if the recognizer executable was not found.
   * @throws IOException
   *           if the configuration file could not be loaded.
   */
  public static CoinsRecognizer createInstance() 
  throws FileNotFoundException, IOException
  {
    Properties config = new Properties();
    File cfgFile = new File(System.getenv("UASR_HOME")
      + "-data/coins/Versuch1/info/recognizer.cfg");
    System.out.println("Configuration file: "+cfgFile.getAbsolutePath());
    config.load(new FileInputStream(cfgFile));
      
    CoinsRecognizer instance = new CoinsRecognizer(findExecutable("recognizer"),config);
    return instance;
  }

  /**
   * Simple command line demo.
   * 
   * @param args -- not used --
   */
  public static void main(String[] args)
  {
  	final gui guiProg = new gui();
  	
	try
    {
      System.out.println("Enter \"exit<cr>\" to terminate program.");
      final CoinsRecognizer demo = CoinsRecognizer.createInstance();
      demo.addObserver(new Observer()
      {
        public void update(Observable o, Object arg)
        {
          char   type = ((String)arg).charAt(0);
          String msg  = ((String)arg).substring(1);
          String echo = String.format("\n[REC%c %s]",type,msg);
          decodeMessage.decode(msg);
          guiProg.setRecognized(decodeMessage.getCoin(), decodeMessage.getSure());
          guiProg.repaint();
          
          switch (type)
          {
          case Recognizer.MSGT_OUT: // Fall through
          case Recognizer.MSGT_IN:  // Fall through
          case Recognizer.MSGT_WRP:
            if (!msg.startsWith("gui:"))
              System.out.print(echo);
            break;
          case Recognizer.MSGT_ERR:
            System.err.print(echo);
            break;
          }
        }        
      });
      
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      while (true)
      {
        String input = in.readLine();
        if (input==null || Recognizer.CMD_EXIT.equals(input)) break;
        demo.enterCommand(input);
      }
      
      System.out.print("\nShutting down...");
      demo.terminate();
      demo.dispose();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    System.out.print("\nEnd of main method\n");  
  }

}
