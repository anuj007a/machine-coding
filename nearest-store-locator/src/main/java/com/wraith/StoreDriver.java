package com.wraith;

import com.wraith.model.Location;
import com.wraith.model.Store;
import com.wraith.repository.StoreRepository;
import com.wraith.service.StoreService;
import com.wraith.service.impl.StoreServiceImpl;
import com.wraith.strategy.DistanceStrategy;
import com.wraith.strategy.HaversineDistance;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class StoreDriver {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            StoreRepository repo = new StoreRepository();
            DistanceStrategy strategy = new HaversineDistance();
            StoreService service =
                    new StoreServiceImpl(repo, strategy);

            service.addStore(new Store("1", "Store A",
                    new Location(12.9716, 77.5946))); // Bangalore

            service.addStore(new Store("2", "Store B",
                    new Location(28.7041, 77.1025))); // Delhi

            Location user = new Location(13.0827, 80.2707); // Chennai

            System.out.println(service.findNearest(user).getStoreName());
            System.out.println(
                service.findKNearest(user, 2).stream()
                    .map(s -> s.getStoreId() + " - " + s.getStoreName())
                    .collect(java.util.stream.Collectors.joining(", "))
            );
            System.out.println(service.findKNearest(user, 2));
        }
}