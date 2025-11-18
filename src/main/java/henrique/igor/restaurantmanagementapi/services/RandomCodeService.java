package henrique.igor.restaurantmanagementapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomCodeService {

    public String generate(int amountCharacter){
        Random random = new Random();

        char[] characters = "0123456789".toCharArray();

        char[] generatePassword = new char[amountCharacter];

        for(int i = 0; i < amountCharacter; i++){
            generatePassword[i] = characters[random.nextInt(characters.length)];
        }

        return new String(generatePassword);
    }
}
