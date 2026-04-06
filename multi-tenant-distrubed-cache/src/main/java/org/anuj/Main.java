package org.anuj;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    /*

    Problem Statement:
Multi-Tenant Distributed Cache (L1/L2)

1. Core Objective
Design a thread-safe, multi-tenant caching library/service that provides a unified interface for multiple independent clients. The system must prevent one tenant's traffic from degrading the performance of others while maintaining high availability and consistent data distribution.

2. Functional Requirements
Multi-Level Architecture: Support a 2-tier strategy:

L1 (Local): Fast, in-memory residing within the application process.

L2 (Distributed): Remote cache accessed via network.

Tenant Isolation: Provide logical or physical isolation for different tenants. Each tenant should have its own:

Namespace: Key collisions between Tenant A and Tenant B must be impossible.

Quotas: Configurable max memory and TTL (Time-to-Live) defaults per tenant.

Consistent Hashing: Implement a mechanism to map keys to L2 nodes that minimizes reshuffling during node joins/failures.

Plug-and-Play Eviction: Support per-tenant eviction policies (LRU, LFU, or FIFO) that can be swapped at runtime.

3. Non-Functional Constraints (SLA)
Scalability: Support 100+ tenants with varying load profiles.

Latency: L1 access < 1ms; L2 access < 10ms (p99).

Reliability: 99.99% availability; handle "hot shards" where a specific tenant's key is accessed 100x more than others.
     */
    /*
    Functional requirement
        * Onboarding to cache
        * Update the config
        * Put(tenant, key, value)
        * get(tenant, key)
        * delete (tenant, key)
        * Eviction per tenant
        * TTL Expiry
        * L1 fallback -> L2 -> Populate L1

    Non-functional requirement
    Latency

    Design pattern
    Strategy pattern
        EvictionStrategy
            LRU, LFU, FIFO ( All should support Dynamicallu)
    ConsistenceHashing  = L2 Node
    Singleton


    Class
    CacheService
    L1 Cache
    L2 Cache
    CacheEntry
    MultitenantCache
    TenantCache
    ConsistentHashing
    CacheNode => L2 Node

    EvictionStrategy
        LRU, LFU, or FIFO


    Client -> CacheService -> MultitenantCache -> Tenant A -> L2 Cache -> L2Cache -> ConsistentHashing -> L2 Node
                                                  Tenant B -> L2 Cache -> L2Cache -> ConsistentHashing -> L2 Node

    Data Structure




     */
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }
    }
}