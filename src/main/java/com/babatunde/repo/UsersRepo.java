package com.babatunde.repo;

import com.babatunde.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface UsersRepo  extends JpaRepository<Users, Integer> {
    Optional<Users> findUserByEmail(String email);


}
