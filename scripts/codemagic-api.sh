#!/bin/bash

# Codemagic API Integration Script for Snake Game
# This script demonstrates how to interact with Codemagic.io REST API

set -e

# Configuration - Set these variables
API_TOKEN="${CODEMAGIC_API_TOKEN:-your_api_token_here}"
APP_ID="${CODEMAGIC_APP_ID:-your_app_id_here}"
BASE_URL="https://api.codemagic.io"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Helper functions
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Check if required tools are available
check_dependencies() {
    print_info "Checking dependencies..."
    
    if ! command -v curl &> /dev/null; then
        print_error "curl is required but not installed."
        exit 1
    fi
    
    if ! command -v jq &> /dev/null; then
        print_warning "jq is not installed. JSON output will not be formatted."
    fi
    
    print_success "Dependencies check completed"
}

# Function to make API requests
api_request() {
    local method="$1"
    local endpoint="$2"
    local data="$3"
    
    local curl_cmd="curl -s -X $method"
    curl_cmd="$curl_cmd -H 'Content-Type: application/json'"
    curl_cmd="$curl_cmd -H 'x-auth-token: $API_TOKEN'"
    
    if [ -n "$data" ]; then
        curl_cmd="$curl_cmd -d '$data'"
    fi
    
    curl_cmd="$curl_cmd $BASE_URL$endpoint"
    
    eval $curl_cmd
}

# Get list of applications
list_apps() {
    print_info "Fetching applications..."
    
    response=$(api_request "GET" "/apps")
    
    if command -v jq &> /dev/null; then
        echo "$response" | jq '.'
    else
        echo "$response"
    fi
}

# Get specific application details
get_app() {
    local app_id="$1"
    
    if [ -z "$app_id" ]; then
        app_id="$APP_ID"
    fi
    
    print_info "Fetching application details for: $app_id"
    
    response=$(api_request "GET" "/apps/$app_id")
    
    if command -v jq &> /dev/null; then
        echo "$response" | jq '.'
    else
        echo "$response"
    fi
}

# Start a new build
start_build() {
    local workflow="$1"
    local branch="$2"
    local app_id="$3"
    
    # Set defaults
    workflow="${workflow:-android-workflow}"
    branch="${branch:-main}"
    app_id="${app_id:-$APP_ID}"
    
    print_info "Starting build for app: $app_id"
    print_info "Workflow: $workflow"
    print_info "Branch: $branch"
    
    local build_data="{
        \"appId\": \"$app_id\",
        \"workflowId\": \"$workflow\",
        \"branch\": \"$branch\"
    }"
    
    response=$(api_request "POST" "/builds" "$build_data")
    
    if command -v jq &> /dev/null; then
        echo "$response" | jq '.'
        
        # Extract build ID if successful
        build_id=$(echo "$response" | jq -r '.buildId // empty')
        if [ -n "$build_id" ]; then
            print_success "Build started successfully!"
            print_info "Build ID: $build_id"
            print_info "You can monitor the build at: https://codemagic.io/app/$app_id/build/$build_id"
        fi
    else
        echo "$response"
    fi
}

# Get build history
get_builds() {
    local app_id="$1"
    local limit="$2"
    
    app_id="${app_id:-$APP_ID}"
    limit="${limit:-10}"
    
    print_info "Fetching build history (limit: $limit)..."
    
    response=$(api_request "GET" "/builds?appId=$app_id&limit=$limit")
    
    if command -v jq &> /dev/null; then
        echo "$response" | jq '.'
    else
        echo "$response"
    fi
}

# Get specific build details
get_build() {
    local build_id="$1"
    
    if [ -z "$build_id" ]; then
        print_error "Build ID is required"
        return 1
    fi
    
    print_info "Fetching build details for: $build_id"
    
    response=$(api_request "GET" "/builds/$build_id")
    
    if command -v jq &> /dev/null; then
        echo "$response" | jq '.'
    else
        echo "$response"
    fi
}

# Cancel a build
cancel_build() {
    local build_id="$1"
    
    if [ -z "$build_id" ]; then
        print_error "Build ID is required"
        return 1
    fi
    
    print_info "Cancelling build: $build_id"
    
    response=$(api_request "POST" "/builds/$build_id/cancel")
    
    if command -v jq &> /dev/null; then
        echo "$response" | jq '.'
    else
        echo "$response"
    fi
}

# Show usage information
show_usage() {
    echo "Codemagic API Integration Script for Snake Game"
    echo ""
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  apps                    List all applications"
    echo "  app [APP_ID]           Get application details"
    echo "  build [WORKFLOW] [BRANCH] [APP_ID]  Start a new build"
    echo "  builds [APP_ID] [LIMIT] Get build history"
    echo "  get-build BUILD_ID     Get specific build details"
    echo "  cancel BUILD_ID        Cancel a running build"
    echo "  help                   Show this help message"
    echo ""
    echo "Environment Variables:"
    echo "  CODEMAGIC_API_TOKEN    Your Codemagic API token (required)"
    echo "  CODEMAGIC_APP_ID       Your application ID (optional)"
    echo ""
    echo "Examples:"
    echo "  $0 apps"
    echo "  $0 build android-workflow main"
    echo "  $0 builds your-app-id 5"
    echo "  $0 get-build build-id-here"
    echo "  $0 cancel build-id-here"
}

# Validate API token
validate_config() {
    if [ "$API_TOKEN" = "your_api_token_here" ] || [ -z "$API_TOKEN" ]; then
        print_error "Please set your Codemagic API token:"
        print_info "export CODEMAGIC_API_TOKEN=your_actual_token"
        print_info "Or edit this script to set the API_TOKEN variable"
        exit 1
    fi
}

# Main script logic
main() {
    check_dependencies
    validate_config
    
    case "${1:-help}" in
        "apps")
            list_apps
            ;;
        "app")
            get_app "$2"
            ;;
        "build")
            start_build "$2" "$3" "$4"
            ;;
        "builds")
            get_builds "$2" "$3"
            ;;
        "get-build")
            get_build "$2"
            ;;
        "cancel")
            cancel_build "$2"
            ;;
        "help"|*)
            show_usage
            ;;
    esac
}

# Run main function with all arguments
main "$@"