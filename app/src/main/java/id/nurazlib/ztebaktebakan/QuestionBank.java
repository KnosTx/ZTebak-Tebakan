package id.nurazlib.ztebaktebakan;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        // Level 1 (1-10)
        questions.add(new Question("Apa ibu kota Indonesia?", new String[]{"Jakarta", "Surabaya", "Bandung", "Medan"}, "Jakarta", "Kota ini memiliki Monas."));
        questions.add(new Question("Gunung tertinggi di dunia?", new String[]{"Kilimanjaro", "Everest", "Elbrus", "Denali"}, "Everest", "Gunung ini berada di Himalaya."));
        questions.add(new Question("Hewan tercepat di darat?", new String[]{"Cheetah", "Singa", "Elang", "Harimau"}, "Cheetah", "Hewan ini bisa berlari hingga 120 km/jam."));
        questions.add(new Question("Planet terdekat dengan Matahari?", new String[]{"Venus", "Mars", "Merkurius", "Bumi"}, "Merkurius", "Planet ini mengalami suhu ekstrem."));
        questions.add(new Question("Benua terbesar di dunia?", new String[]{"Afrika", "Asia", "Eropa", "Amerika Utara"}, "Asia", "Benua ini memiliki jumlah penduduk terbesar."));
        questions.add(new Question("Apa ibu kota Jepang?", new String[]{"Tokyo", "Kyoto", "Osaka", "Hokkaido"}, "Tokyo", "Kota ini terkenal dengan Menara Tokyo."));
        questions.add(new Question("Bahasa resmi Brasil?", new String[]{"Spanyol", "Portugis", "Inggris", "Prancis"}, "Portugis", "Bahasa ini juga digunakan di Portugal."));
        questions.add(new Question("Hewan nasional Australia?", new String[]{"Kanguru", "Koala", "Platipus", "Emu"}, "Kanguru", "Hewan ini memiliki kantung di perutnya."));
        questions.add(new Question("Sungai terpanjang di dunia?", new String[]{"Nil", "Amazon", "Yangtze", "Mississippi"}, "Nil", "Sungai ini mengalir melalui Mesir."));
        questions.add(new Question("Siapa penemu telepon?", new String[]{"Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "James Watt"}, "Alexander Graham Bell", "Penemuannya mengubah cara manusia berkomunikasi."));

        // Level 2 (11-20)
        questions.add(new Question("Dimana Menara Eiffel berada?", new String[]{"Paris", "Berlin", "London", "Madrid"}, "Paris", "Kota ini disebut 'Kota Cinta'."));
        questions.add(new Question("Apa tiga warna primer?", new String[]{"Merah, Kuning, Biru", "Hijau, Oranye, Ungu", "Hitam, Putih, Abu-Abu", "Merah, Hijau, Biru"}, "Merah, Kuning, Biru", "Warna dasar yang tidak bisa dibuat dari warna lain."));
        questions.add(new Question("Siapa ilmuwan yang menemukan hukum gravitasi?", new String[]{"Isaac Newton", "Albert Einstein", "Galileo Galilei", "Nikola Tesla"}, "Isaac Newton", "Ia terinspirasi oleh apel yang jatuh dari pohon."));
        questions.add(new Question("Apa simbol kimia untuk emas?", new String[]{"Au", "Ag", "Fe", "Hg"}, "Au", "Simbol ini berasal dari bahasa Latin 'Aurum'."));
        questions.add(new Question("Lagu kebangsaan Indonesia adalah?", new String[]{"Indonesia Raya", "Garuda Pancasila", "Tanah Airku", "Halo-Halo Bandung"}, "Indonesia Raya", "Diciptakan oleh W.R. Supratman."));
        questions.add(new Question("Negara mana yang memiliki bendera hanya berwarna merah dan putih?", new String[]{"Indonesia", "Singapura", "Polandia", "Monako"}, "Indonesia", "Bendera ini disebut 'Sang Saka Merah Putih'."));
        questions.add(new Question("Siapa pencipta lampu pijar yang paling terkenal?", new String[]{"Thomas Edison", "Nikola Tesla", "Alexander Graham Bell", "Benjamin Franklin"}, "Thomas Edison", "Ia melakukan lebih dari 1.000 percobaan."));
        questions.add(new Question("Samudra terluas di dunia adalah?", new String[]{"Samudra Pasifik", "Samudra Atlantik", "Samudra Hindia", "Samudra Arktik"}, "Samudra Pasifik", "Samudra ini mencakup sepertiga permukaan bumi."));
        questions.add(new Question("Siapa Presiden ke-3 Indonesia?", new String[]{"Soekarno", "Gus Dur", "Jokowi", "B.J Habibie"}, "B.J Habibie", "Beliau dikenal sebagai ahli pesawat terbang."));
        questions.add(new Question("Negara dengan jumlah penduduk terbanyak di dunia (data 2024)?", new String[]{"Cina", "India", "Amerika Serikat", "Indonesia"}, "India", "Negara ini menyalip Tiongkok pada tahun 2023."));

        // Level 3 (21-30)
        questions.add(new Question("Apa mata uang Jepang?", new String[]{"Yen", "Won", "Ringgit", "Baht"}, "Yen", "Mata uang ini memiliki simbol ¥."));
        questions.add(new Question("Siapa penulis novel 'Laskar Pelangi'?", new String[]{"Andrea Hirata", "Tere Liye", "Pramoedya Ananta Toer", "Habiburrahman El Shirazy"}, "Andrea Hirata", "Buku ini bercerita tentang anak-anak di Belitung."));
        questions.add(new Question("Planet terbesar di Tata Surya?", new String[]{"Jupiter", "Saturnus", "Uranus", "Neptunus"}, "Jupiter", "Planet ini adalah raksasa gas."));
        questions.add(new Question("Apa nama samudra terkecil di dunia?", new String[]{"Samudra Arktik", "Samudra Hindia", "Samudra Atlantik", "Samudra Selatan"}, "Samudra Arktik", "Samudra ini berada di sekitar Kutub Utara."));
        questions.add(new Question("Siapa yang melukis Mona Lisa?", new String[]{"Leonardo da Vinci", "Vincent van Gogh", "Pablo Picasso", "Michelangelo"}, "Leonardo da Vinci", "Ia adalah seorang seniman dan ilmuwan dari Italia."));
        questions.add(new Question("Negara mana yang dikenal sebagai 'Negeri Kincir Angin'?", new String[]{"Belanda", "Denmark", "Jerman", "Belgia"}, "Belanda", "Negara ini menggunakan kincir angin untuk mengeringkan daratan."));
        questions.add(new Question("Apa komponen gas terbesar di udara yang kita hirup?", new String[]{"Nitrogen", "Oksigen", "Karbon Dioksida", "Argon"}, "Nitrogen", "Kandungannya sekitar 78% di atmosfer."));
        questions.add(new Question("Berapa jumlah provinsi di Indonesia per tahun 2024?", new String[]{"34", "37", "38", "39"}, "38", "Provinsi baru ditambahkan di wilayah Papua."));
        questions.add(new Question("Apa nama ibu kota Thailand?", new String[]{"Bangkok", "Hanoi", "Kuala Lumpur", "Manila"}, "Bangkok", "Kota ini terkenal dengan kuil-kuilnya yang megah."));
        questions.add(new Question("Hewan darat terbesar di dunia?", new String[]{"Gajah Afrika", "Badak Putih", "Kuda Nil", "Jerapah"}, "Gajah Afrika", "Hewan ini memiliki belalai yang panjang dan kuat."));

        // Level 4 (31-40)
        questions.add(new Question("Siapa pahlawan yang fotonya ada di uang Rp100.000?", new String[]{"Soekarno & Hatta", "Cut Nyak Dien", "I Gusti Ngurah Rai", "Pangeran Diponegoro"}, "Soekarno & Hatta", "Mereka adalah proklamator kemerdekaan Indonesia."));
        questions.add(new Question("Apa nama selat yang memisahkan Pulau Jawa dan Sumatra?", new String[]{"Selat Sunda", "Selat Malaka", "Selat Bali", "Selat Karimata"}, "Selat Sunda", "Di selat ini terdapat gunung Anak Krakatau."));
        questions.add(new Question("Apa julukan untuk kota Yogyakarta?", new String[]{"Kota Pelajar", "Kota Kembang", "Kota Pahlawan", "Kota Hujan"}, "Kota Pelajar", "Kota ini memiliki banyak universitas ternama."));
        questions.add(new Question("Dalam cerita pewayangan, siapa nama ayah dari Gatotkaca?", new String[]{"Bima", "Arjuna", "Yudistira", "Nakula"}, "Bima", "Ia adalah salah satu dari Pandawa Lima."));
        questions.add(new Question("Apa nama candi Buddha terbesar di dunia yang ada di Indonesia?", new String[]{"Borobudur", "Prambanan", "Angkor Wat", "Shwedagon"}, "Borobudur", "Candi ini terletak di Magelang, Jawa Tengah."));
        questions.add(new Question("Siapa yang menjahit Bendera Merah Putih pertama kali?", new String[]{"Fatmawati", "Cut Nyak Dien", "R.A. Kartini", "Martha Christina Tiahahu"}, "Fatmawati", "Beliau adalah istri dari Presiden Soekarno."));
        questions.add(new Question("Apa nama ibu kota Korea Selatan?", new String[]{"Seoul", "Busan", "Incheon", "Daegu"}, "Seoul", "Kota ini terkenal dengan musik K-Pop dan teknologinya."));
        questions.add(new Question("Makanan khas dari Padang yang terkenal di seluruh dunia?", new String[]{"Rendang", "Sate", "Gudeg", "Soto"}, "Rendang", "Pernah dinobatkan sebagai makanan terlezat di dunia."));
        questions.add(new Question("Pulau terbesar di Indonesia adalah?", new String[]{"Kalimantan", "Sumatra", "Jawa", "Papua"}, "Kalimantan", "Pulau ini dibagi menjadi wilayah Indonesia, Malaysia, dan Brunei."));
        questions.add(new Question("Siapa salah satu perumus naskah proklamasi kemerdekaan Indonesia?", new String[]{"Soekarno", "Mohammad Yamin", "Sutan Sjahrir", "Ki Hajar Dewantara"}, "Soekarno", "Ia juga yang membacakan teks proklamasi."));

        return questions;
    }
}

