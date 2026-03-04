#!/bin/bash

# Skill Nest - Unified Application Launcher
# This script starts all backend microservices and the frontend together

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# PID tracking for cleanup
PIDS=()

# Cleanup function
cleanup() {
    echo -e "\n${YELLOW}Shutting down all services...${NC}"
    for pid in "${PIDS[@]}"; do
        if kill -0 "$pid" 2>/dev/null; then
            echo -e "${YELLOW}Stopping process $pid${NC}"
            kill "$pid" 2>/dev/null || true
        fi
    done
    # Kill any remaining Java processes from our services
    pkill -f "api-gateway" 2>/dev/null || true
    pkill -f "auth-service" 2>/dev/null || true
    pkill -f "user-service" 2>/dev/null || true
    pkill -f "course-service" 2>/dev/null || true
    pkill -f "assignment-service" 2>/dev/null || true
    echo -e "${GREEN}All services stopped.${NC}"
    exit 0
}

# Register cleanup on script exit
trap cleanup SIGINT SIGTERM EXIT

# Source SDKMAN for Java 21
echo -e "${BLUE}Setting up Java environment...${NC}"
if [ -f "/usr/local/sdkman/bin/sdkman-init.sh" ]; then
    source /usr/local/sdkman/bin/sdkman-init.sh
    sdk use java 21.0.10-amzn 2>/dev/null || true
fi

# Verify Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1)
echo -e "${GREEN}Using: $JAVA_VERSION${NC}"

# Navigate to project root
cd "$(dirname "$0")"
PROJECT_ROOT=$(pwd)

# Build backend services
echo -e "\n${BLUE}========================================${NC}"
echo -e "${BLUE}Building Backend Microservices...${NC}"
echo -e "${BLUE}========================================${NC}"

cd "$PROJECT_ROOT/backend"
mvn clean install -DskipTests -q

# Start API Gateway (port 8080)
echo -e "\n${GREEN}Starting API Gateway on port 8080...${NC}"
cd "$PROJECT_ROOT/backend/api-gateway"
mvn spring-boot:run -q &
PIDS+=($!)
sleep 3

# Start Auth Service (port 8081)
echo -e "${GREEN}Starting Auth Service on port 8081...${NC}"
cd "$PROJECT_ROOT/backend/auth-service"
mvn spring-boot:run -q &
PIDS+=($!)
sleep 3

# Start User Service (port 8082)
echo -e "${GREEN}Starting User Service on port 8082...${NC}"
cd "$PROJECT_ROOT/backend/user-service"
mvn spring-boot:run -q &
PIDS+=($!)
sleep 3

# Start Course Service (port 8083)
echo -e "${GREEN}Starting Course Service on port 8083...${NC}"
cd "$PROJECT_ROOT/backend/course-service"
mvn spring-boot:run -q &
PIDS+=($!)
sleep 3

# Start Assignment Service (port 8084)
echo -e "${GREEN}Starting Assignment Service on port 8084...${NC}"
cd "$PROJECT_ROOT/backend/assignment-service"
mvn spring-boot:run -q &
PIDS+=($!)
sleep 5

# Install and start frontend
echo -e "\n${BLUE}========================================${NC}"
echo -e "${BLUE}Starting Frontend...${NC}"
echo -e "${BLUE}========================================${NC}"

cd "$PROJECT_ROOT/frontend"
npm install --silent
npm run dev &
PIDS+=($!)

echo -e "\n${GREEN}========================================${NC}"
echo -e "${GREEN}All services started successfully!${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e ""
echo -e "${BLUE}Frontend:${NC}           http://localhost:5173"
echo -e "${BLUE}API Gateway:${NC}        http://localhost:8080"
echo -e "${BLUE}Auth Service:${NC}       http://localhost:8081"
echo -e "${BLUE}User Service:${NC}       http://localhost:8082"
echo -e "${BLUE}Course Service:${NC}     http://localhost:8083"
echo -e "${BLUE}Assignment Service:${NC} http://localhost:8084"
echo -e ""
echo -e "${YELLOW}Press Ctrl+C to stop all services${NC}"
echo -e ""

# Wait for all background processes
wait
