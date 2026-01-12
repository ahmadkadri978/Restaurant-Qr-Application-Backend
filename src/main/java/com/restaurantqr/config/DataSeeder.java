package com.restaurantqr.config;

import com.restaurantqr.user.Role;
import com.restaurantqr.menu.MenuCategory;
import com.restaurantqr.menu.MenuCategoryRepository;
import com.restaurantqr.menu.MenuItem;
import com.restaurantqr.menu.MenuItemRepository;
import com.restaurantqr.restaurant.Restaurant;
import com.restaurantqr.restaurant.RestaurantRepository;
import com.restaurantqr.table.RestaurantTable;
import com.restaurantqr.table.RestaurantTableRepository;
import com.restaurantqr.user.User;
import com.restaurantqr.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            RestaurantRepository restaurantRepository,
            UserRepository userRepository,
            RestaurantTableRepository tableRepository,
            MenuCategoryRepository categoryRepository,
            MenuItemRepository itemRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // ✅ Seed only once (simple approach)
            if (userRepository.count() > 0) {
                return;
            }

            // 1) Restaurant
            Restaurant restaurant = new Restaurant();
            restaurant.setName("Demo Restaurant");
            restaurant.setCode("DEMO");
            restaurant.setActive(true);
            restaurantRepository.save(restaurant);

            // 2) Users
            User manager = new User();
            manager.setUsername("manager1");
            manager.setPasswordHash(passwordEncoder.encode("123456"));
            manager.setRole(Role.MANAGER);
            manager.setRestaurant(restaurant);
            manager.setActive(true);
            userRepository.save(manager);

            User staff = new User();
            staff.setUsername("staff1");
            staff.setPasswordHash(passwordEncoder.encode("123456"));
            staff.setRole(Role.STAFF);
            staff.setRestaurant(restaurant);
            staff.setActive(true);
            userRepository.save(staff);

            // 3) Table
            // ✅ ثابت للتجربة: ستستخدمه في endpoint /public/tables/{qrToken}/menu
            String demoQrToken = "DEMO-TABLE-1";

            RestaurantTable table = new RestaurantTable();
            table.setRestaurant(restaurant);
            table.setTableNumber(1);
            table.setQrToken(demoQrToken);
            table.setActive(true);
            tableRepository.save(table);

            // 4) Category
            MenuCategory drinks = new MenuCategory();
            drinks.setRestaurant(restaurant);
            drinks.setName("Drinks");
            drinks.setDisplayOrder(1);
            drinks.setActive(true);
            categoryRepository.save(drinks);

            // 5) Items
            MenuItem cola = new MenuItem();
            cola.setRestaurant(restaurant);
            cola.setCategory(drinks);
            cola.setName("Cola");
            cola.setDescription("Cold cola can");
            cola.setPrice(new BigDecimal("2.50"));
            cola.setAvailable(true);
            cola.setActive(true);
            cola.setDisplayOrder(1);
            itemRepository.save(cola);

            MenuItem water = new MenuItem();
            water.setRestaurant(restaurant);
            water.setCategory(drinks);
            water.setName("Water");
            water.setDescription("Still water bottle");
            water.setPrice(new BigDecimal("1.50"));
            water.setAvailable(false); // ❌ غير متوفر (لن يظهر للزبون)
            water.setActive(true);
            water.setDisplayOrder(2);
            itemRepository.save(water);

            System.out.println("✅ Seeded Restaurant + Users + Table + Menu");
            System.out.println("➡ MANAGER login: manager1 / 123456");
            System.out.println("➡ STAFF   login: staff1   / 123456");
            System.out.println("➡ Demo Table QR Token: " + demoQrToken);
        };
    }
}
