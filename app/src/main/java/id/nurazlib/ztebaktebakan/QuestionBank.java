package id.nurazlib.ztebaktebakan;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question("Apa ibu kota Indonesia?", 
            new String[]{"Jakarta", "Surabaya", "Bandung", "Medan"}, 
            "Jakarta", "Kota ini memiliki Monas."));
        
        questions.add(new Question("Gunung tertinggi di dunia?", 
            new String[]{"Kilimanjaro", "Everest", "Elbrus", "Denali"}, 
            "Everest", "Gunung ini berada di Himalaya."));
        
        questions.add(new Question("Hewan tercepat di dunia?", 
            new String[]{"Cheetah", "Singa", "Elang", "Harimau"}, 
            "Cheetah", "Hewan ini bisa berlari hingga 120 km/jam."));
        
        questions.add(new Question("Planet terdekat dengan Matahari?", 
            new String[]{"Venus", "Mars", "Merkurius", "Bumi"}, 
            "Merkurius", "Planet ini mengalami suhu ekstrem."));
        
        questions.add(new Question("Benua terbesar di dunia?", 
            new String[]{"Afrika", "Asia", "Eropa", "Amerika Utara"}, 
            "Asia", "Benua ini memiliki jumlah penduduk terbesar."));
        
        questions.add(new Question("Apa ibu kota Jepang?", 
            new String[]{"Tokyo", "Kyoto", "Osaka", "Hokkaido"}, 
            "Tokyo", "Kota ini terkenal dengan Menara Tokyo."));
        
        questions.add(new Question("Bahasa resmi Brasil?", 
            new String[]{"Spanyol", "Portugis", "Inggris", "Prancis"}, 
            "Portugis", "Bahasa ini juga digunakan di Portugal."));
        
        questions.add(new Question("Hewan nasional Australia?", 
            new String[]{"Kanguru", "Koala", "Platipus", "Emu"}, 
            "Kanguru", "Hewan ini bisa melompat jauh."));
        
        questions.add(new Question("Sungai terpanjang di dunia?", 
            new String[]{"Nil", "Amazon", "Yangtze", "Mississippi"}, 
            "Nil", "Sungai ini mengalir melalui Mesir."));
        
        questions.add(new Question("Siapa penemu telepon?", 
            new String[]{"Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "James Watt"}, 
            "Alexander Graham Bell", "Penemuannya mengubah cara manusia berkomunikasi."));

        questions.add(new Question("Dimana Menara Eiffel berada?", 
            new String[]{"Paris", "Berlin", "London", "Madrid"}, 
            "Paris", "Kota ini disebut 'Kota Cinta'."));

        questions.add(new Question("Apa warna primer?", 
            new String[]{"Merah, Kuning, Biru", "Hijau, Oranye, Ungu", "Hitam, Putih, Abu-Abu", "Merah, Hijau, Biru"}, 
            "Merah, Kuning, Biru", "Warna-warna dasar yang tidak bisa dibuat dari warna lain."));

        questions.add(new Question("Siapa ilmuwan yang menemukan hukum gravitasi?", 
            new String[]{"Isaac Newton", "Albert Einstein", "Galileo Galilei", "Nikola Tesla"}, 
            "Isaac Newton", "Ia melihat apel jatuh dari pohon."));

        questions.add(new Question("Apa simbol kimia untuk emas?", 
            new String[]{"Au", "Ag", "Fe", "Hg"}, 
            "Au", "Simbol ini berasal dari bahasa Latin 'Aurum'."));

        questions.add(new Question("Lagu kebangsaan Indonesia adalah?", 
            new String[]{"Indonesia Raya", "Garuda Pancasila", "Tanah Airku", "Halo-Halo Bandung"}, 
            "Indonesia Raya", "Dinyanyikan saat upacara bendera."));

        questions.add(new Question("Negara mana yang memiliki bendera berwarna merah dan putih?", 
            new String[]{"Indonesia", "Singapura", "Malaysia", "Thailand"}, 
            "Indonesia", "Bendera ini disebut 'Sang Saka Merah Putih'."));

        questions.add(new Question("Siapa pencipta lampu pijar?", 
            new String[]{"Thomas Edison", "Nikola Tesla", "Alexander Graham Bell", "Benjamin Franklin"}, 
            "Thomas Edison", "Ia melakukan lebih dari 1.000 percobaan."));

        questions.add(new Question("Laut terbesar di dunia adalah?", 
            new String[]{"Samudra Pasifik", "Samudra Atlantik", "Samudra Hindia", "Laut Cina Selatan"}, 
            "Samudra Pasifik", "Laut ini mencakup sepertiga permukaan bumi."));

        questions.add(new Question("Siapa Presiden ke-3 Indonesia?", 
            new String[]{"Soekarno", "Gus Dur", "Jokowi", "B.J Habibie"}, 
            "B.J Habibie", "Beliau dikenal sebagai ahli pesawat terbang."));

        questions.add(new Question("Negara dengan jumlah penduduk terbanyak?", 
            new String[]{"Cina", "India", "Amerika Serikat", "Indonesia"}, 
            "Cina", "Negara ini memiliki populasi lebih dari 1 miliar orang."));

        questions.add(new Question("Apa mata uang Jepang?", 
            new String[]{"Yen", "Won", "Ringgit", "Baht"}, 
            "Yen", "Mata uang ini memiliki simbol ¥."));

        questions.add(new Question("Siapa penulis novel 'Laskar Pelangi'?", 
            new String[]{"Andrea Hirata", "Tere Liye", "Pramoedya Ananta Toer", "Habiburrahman El Shirazy"}, 
            "Andrea Hirata", "Buku ini bercerita tentang anak-anak di Belitung."));
        return questions;
    }
}
