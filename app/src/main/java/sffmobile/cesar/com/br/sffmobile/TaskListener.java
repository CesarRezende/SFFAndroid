package sffmobile.cesar.com.br.sffmobile;

import java.util.List;

public interface TaskListener {
    void onTaskStarted(int requestCode);

	void onTaskFinished(int requestCode, List<String> errorMessages);

}
