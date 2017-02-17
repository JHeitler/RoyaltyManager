package royalties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import royalties.dal.RoyaltyDao;
import royalties.dal.RoyaltyDaoJsonFileImpl;
import royalties.requests.ViewingHandler;


@SpringBootApplication
public class Royalties {
    public static void main(String[] args) {
        SpringApplication.run(Royalties.class, args);
    }
}
