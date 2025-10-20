# API Interface Automation Test

## Overview
This is the API interface automation test project for the Lottery System, based on Python + Pytest + Allure framework.

## Test Statistics
- **Total Test Cases**: 41
- **Pass Rate**: 100%
- **Test Framework**: Python + Pytest + Allure
- **Test Date**: October 20, 2025

## Project Structure
```
1-API-Interface-Automation-Test/
├── cases/                    # Test case modules
│   ├── test_auth.py         # Authentication module tests
│   ├── test_user.py         # User management tests  
│   ├── test_lottery.py      # Lottery function tests
│   ├── test_prize.py        # Prize management tests
│   ├── test_activity.py     # Activity management tests
│   └── test_user_prize_record.py  # User prize record tests
├── utils/                   # Utility modules
│   ├── request_util.py      # HTTP request utility
│   ├── yaml_util.py         # YAML file utility
│   └── logger_util.py       # Logger utility
├── data/                    # Test data
│   └── data.yml            # Test configuration data
├── pytest.ini               # Pytest configuration
├── run.py                   # Test execution script
└── requirements.txt         # Python dependencies
```

## Test Modules

### 1. Authentication Module (test_auth.py)
- Token verification
- Token refresh
- Current user info query
- User logout

### 2. User Management (test_user.py)  
- User registration
- User login
- User info query
- User password update
- User status management

### 3. Lottery Function (test_lottery.py)
- Participate in lottery
- Query lottery results
- Lottery validation

### 4. Prize Management (test_prize.py)
- Prize list query
- Prize details query
- Prize creation
- Prize update
- Prize deletion

### 5. Activity Management (test_activity.py)
- Activity list query
- Activity details query
- Activity creation
- Activity update

### 6. User Prize Records (test_user_prize_record.py)
- Prize record query
- Record validation

## Environment Setup

### 1. Install Python
Requires Python 3.8 or higher

### 2. Install Dependencies
```bash
pip install -r requirements.txt
```

Main dependencies:
- pytest
- requests
- allure-pytest
- pyyaml
- jsonschema

### 3. Configure Test Environment
Edit `utils/request_util.py` to set API server address:
```python
host = 'http://101.42.36.43:8888'
```

## Run Tests

### Run All Tests
```bash
python run.py
```

### Run Specific Module
```bash
pytest cases/test_auth.py -v
```

### Run Specific Test Case
```bash
pytest cases/test_auth.py::TestAuth::test_token_verify -v
```

### Generate Allure Report
```bash
# Execute tests and generate allure results
pytest --alluredir=./allure-results

# Generate HTML report
allure generate allure-results -o allure-report --clean

# Open report
allure open allure-report
```

## Test Data Configuration
Test data is configured in `data/data.yml`:
```yaml
test_user:
  email: "test@example.com"
  password: "password123"
  
api_config:
  base_url: "http://101.42.36.43:8888"
  timeout: 30
```

## Notes
1. Ensure API server is running before testing
2. Some tests require admin privileges
3. Database state may affect test results, it's recommended to use test database
4. Test cases have execution order dependencies, use pytest-order plugin for control

## View Test Report
After running tests, view generated Allure report:
```bash
allure open allure-html
```

Or view the report directly in main project:
```
../4-All-Test-Reports/api_test_report/index.html
```

## Contact
- Test Engineer: JPF
- Test Date: October 20, 2025

