package net.sunny.web.italker.push.factory;

import net.sunny.web.italker.push.bean.api.feedback.FeedbackModel;
import net.sunny.web.italker.push.bean.db.Feedback;
import net.sunny.web.italker.push.bean.db.User;
import net.sunny.web.italker.push.utils.Hib;

public class FeedbackFactory {

    public static Feedback add(User self, FeedbackModel model) {
        Feedback feedback = new Feedback(self, model);

        return save(feedback);
    }

    private static Feedback save(Feedback feedback) {
        return Hib.save(session -> {
            session.save(feedback);
            session.flush();
            session.refresh(feedback);
            return feedback;
        });
    }
}
