package id.nurazlib.ztebaktebakan;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question("Apa ibu kota Indonesia?", 
            new String[]{"Jakarta", "Surabaya", "Bandung", "Medan"}, "Jakarta"));
        
        questions.add(new Question("Gunung tertinggi di dunia?", 
            new String[]{"Kilimanjaro", "Everest", "Elbrus", "Denali"}, "Everest"));
        
        questions.add(new Question("Hewan tercepat di dunia?", 
            new String[]{"Cheetah", "Singa", "Elang", "Harimau"}, "Cheetah"));
        
        questions.add(new Question("Planet terdekat dengan Matahari?", 
            new String[]{"Venus", "Mars", "Merkurius", "Bumi"}, "Merkurius"));
        
        questions.add(new Question("Benua terbesar di dunia?", 
            new String[]{"Afrika", "Asia", "Eropa", "Amerika Utara"}, "Asia"));
        
        questions.add(new Question("Apa ibu kota Jepang?", 
            new String[]{"Tokyo", "Kyoto", "Osaka", "Hokkaido"}, "Tokyo"));
        
        questions.add(new Question("Bahasa resmi Brasil?", 
            new String[]{"Spanyol", "Portugis", "Inggris", "Prancis"}, "Portugis"));
        
        questions.add(new Question("Hewan nasional Australia?", 
            new String[]{"Kanguru", "Koala", "Platipus", "Emu"}, "Kanguru"));
        
        questions.add(new Question("Sungai terpanjang di dunia?", 
            new String[]{"Nil", "Amazon", "Yangtze", "Mississippi"}, "Nil"));
        
        questions.add(new Question("Siapa penemu telepon?", 
            new String[]{"Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "James Watt"}, "Alexander Graham Bell"));

        questions.add(new Question("Planet terbesar di tata surya?", 
            new String[]{"Jupiter", "Saturnus", "Neptunus", "Mars"}, "Jupiter"));
        
        questions.add(new Question("Siapa penulis 'Harry Potter'?", 
            new String[]{"J.K. Rowling", "J.R.R. Tolkien", "George R.R. Martin", "Stephen King"}, "J.K. Rowling"));
        
        questions.add(new Question("Apa ibu kota Mesir?", 
            new String[]{"Kairo", "Alexandria", "Giza", "Luxor"}, "Kairo"));
        
        questions.add(new Question("Siapa Presiden pertama Indonesia?", 
            new String[]{"Soekarno", "Soeharto", "Habibie", "Gus Dur"}, "Soekarno"));
        
        questions.add(new Question("Hewan terbesar di dunia?", 
            new String[]{"Gajah", "Paus Biru", "Beruang", "Jerapah"}, "Paus Biru"));
        
        questions.add(new Question("Apa ibu kota Prancis?", 
            new String[]{"Paris", "Marseille", "Lyon", "Toulouse"}, "Paris"));
        
        questions.add(new Question("Benua terkecil di dunia?", 
            new String[]{"Australia", "Antartika", "Eropa", "Amerika Selatan"}, "Australia"));
        
        questions.add(new Question("Hewan yang bisa terbang?", 
            new String[]{"Kelelawar", "Ayam", "Burung Unta", "Penguin"}, "Kelelawar"));
        
        questions.add(new Question("Logam mulia yang berwarna kuning?", 
            new String[]{"Emas", "Perak", "Tembaga", "Besi"}, "Emas"));
        
        questions.add(new Question("Planet merah?", 
            new String[]{"Mars", "Venus", "Jupiter", "Saturnus"}, "Mars"));

        questions.add(new Question("Siapa Presiden ke-3 Indonesia?",
            new String[]{"Soekarno", "Gus Dur", "Jokowi", "B.J Habibie"}, "B.J Habibie"));

        // Pertanyaan tambahan
        questions.add(new Question("Dimana Menara Eiffel berada?", 
            new String[]{"Paris", "Berlin", "London", "Madrid"}, "Paris"));

        questions.add(new Question("Apa warna primer?", 
            new String[]{"Merah, Kuning, Biru", "Hijau, Oranye, Ungu", "Hitam, Putih, Abu-Abu", "Merah, Hijau, Biru"}, "Merah, Kuning, Biru"));

        questions.add(new Question("Siapa ilmuwan yang menemukan hukum gravitasi?", 
            new String[]{"Isaac Newton", "Albert Einstein", "Galileo Galilei", "Nikola Tesla"}, "Isaac Newton"));

        questions.add(new Question("Apa simbol kimia untuk emas?", 
            new String[]{"Au", "Ag", "Fe", "Hg"}, "Au"));

        questions.add(new Question("Lagu kebangsaan Indonesia adalah?", 
            new String[]{"Indonesia Raya", "Garuda Pancasila", "Tanah Airku", "Halo-Halo Bandung"}, "Indonesia Raya"));

        questions.add(new Question("Negara mana yang memiliki bendera berwarna merah dan putih?", 
            new String[]{"Indonesia", "Singapura", "Malaysia", "Thailand"}, "Indonesia"));

        questions.add(new Question("Siapa pencipta lampu pijar?", 
            new String[]{"Thomas Edison", "Nikola Tesla", "Alexander Graham Bell", "Benjamin Franklin"}, "Thomas Edison"));

        questions.add(new Question("Laut terbesar di dunia adalah?", 
            new String[]{"Samudra Pasifik", "Samudra Atlantik", "Samudra Hindia", "Laut Cina Selatan"}, "Samudra Pasifik"));

        return questions;
    }
}
