package ua.ypon.sensor.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ua.ypon 15.10.2023
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ua.ypon.sensor.controllers")
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/measurements/measurements-chart-data") // Додайте URL вашого контролера
                .allowedOrigins("http://localhost:63342") // Дозволити запити з цього джерела
                .allowedMethods("GET"); // Дозволити тільки GET-запити
    }
}
/*
Цей код налагоджує конфігурацію для веб-застосунка на основі Spring Framework. Ось пояснення до кожної частини коду:

package ua.ypon.sensor.config;: Це оголошення пакету, яке визначає, в якому пакеті розташований цей клас конфігурації.

@Configuration: Ця анотація позначає клас як конфігураційний клас Spring, який містить налаштування для додатка.

@EnableWebMvc: Ця анотація вказує, що ми включаємо конфігурацію для обробки веб-запитів.

@ComponentScan(basePackages = "ua.ypon.sensor.controllers"): Ця анотація вказує Spring сканувати пакет "ua.ypon.sensor.controllers" для пошуку контролерів.

WebMvcConfigurer: Цей клас реалізує інтерфейс WebMvcConfigurer, який дозволяє налаштувати параметри веб-застосунка.

addCorsMappings(CorsRegistry registry): Цей метод налаштовує політику CORS (Cross-Origin Resource Sharing) для відповідного URL.

registry.addMapping("/measurements/measurements-chart-data"): Вказуємо URL, для якого буде застосовуватися політика CORS. У цьому випадку, це URL для отримання даних графіка.

.allowedOrigins("http://localhost:63342"): Дозволяє запити з вказаного джерела, в даному випадку, з http://localhost:63342. Це важливо для безпеки браузерних AJAX-запитів.

.allowedMethods("GET"): Дозволяє тільки HTTP-запити типу GET. Це обмеження вказує, що дозволено лише отримання (запит) даних.

Отже, цей конфігураційний клас дозволяє налаштувати політику CORS для конкретного URL, щоб дозволити звертатися до додатка з певного джерела (у цьому випадку, http://localhost:63342) та зазначає типи HTTP-запитів, які дозволені (у цьому випадку, лише GET).
 */