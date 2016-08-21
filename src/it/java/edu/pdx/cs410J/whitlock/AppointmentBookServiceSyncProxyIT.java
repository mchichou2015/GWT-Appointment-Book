package edu.pdx.cs410J.whitlock;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import edu.pdx.cs410J.whitlock.client.AppointmentBook;
import edu.pdx.cs410J.whitlock.client.AppointmentBookService;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class AppointmentBookServiceSyncProxyIT extends HttpRequestHelper {

  private final int httpPort = Integer.getInteger("http.port", 8080);
  private String webAppUrl = "http://localhost:" + httpPort + "/apptbook";



}
