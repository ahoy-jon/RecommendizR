package services;

import java.io.IOException;

import play.Logger;
import play.jobs.Job;
import play.jobs.On;

/**
 * @author Jean-Baptiste lemée
 */
@On("0 * * * * ?")
public class UpdateIndexesJob extends Job {

    public void doJob() {
       try {
         SearchService.buildIndexes();
       } catch (IOException e) {
         Logger.error(e, e.getMessage());
       }
    }

}

