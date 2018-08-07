pragma solidity ^0.4.11;

 // ----------------------------------------------------------------------------------------------
 // Sample fixed supply token contract
 // Miranz Technology Pvt. Ltd (2017).
 // Author Hamza Yasin
 // ----------------------------------------------------------------------------------------------

import './SafeMath.sol';
import './ERC20.sol';

// ERC20 Token Smart contract
contract ERC20Token is ERC20 {

    string private constant name  = "DecentralizedDatabase";
    string private constant  symbol = "DD";
    uint8 private constant decimals = 18;
    uint private _totalSupply = 100000;
    uint256 private constant RATE = 500;
    uint private tokenPrice=1;
    using SafeMath for uint256;
    address private owner;
   // event TransferToVault(address indexed _from, address indexed _to, uint256 _value);
   // event TransferFromVault(address from,address to, uint256 _value);
    
    //tracks the amount of locked balances 
    mapping(address=>uint256) vaultBalances;
    
        /**maps lock balance from=>to=>amt**/
    mapping(address=>mapping(address=>uint))lockBalances;
    
    // Owner of account approves the transfer of an amount to another account
    mapping(address => mapping(address=>uint256)) allowed;
    
    // Its a payable function works as a token factory.
     // Functions with this modifier can only be executed by the owner
     modifier onlyOwner() {
        require(msg.sender == owner) ;
         _;
     }

    // Balances for each account
    mapping(address => uint256) balances;
   
    // Its a payable function works as a token factory.
    function () public payable{
    }

    // Constructor
    function ERC20Token()public{
        owner = msg.sender;
        balances[msg.sender]=_totalSupply;
        
    }
    function getOwner()public view returns(address){
        return owner;
    }
    function getTokenPrice()public view returns(uint){
        return tokenPrice;
    }
    function totalSupply() public view returns(uint256){
        return _totalSupply;
    }
    // What is the balance of a particular account?
    function balanceOf(address _owner) public view returns(uint256){
        return balances[_owner];
    }
    
   
 // Transfer the balance from owner's account to another account
    function _transfer(address from,address _to, uint256 _value) internal returns(bool){
        require(balances[from] >= _value && _value > 0 );
        balances[from] = balances[from].sub(_value);
        balances[_to] = balances[_to].add(_value);
       // emit Transfer(from, _to, _value);
        return true;
    }
     // Transfer the balance from owner's account to another account
    function transfer(address _to, uint256 _value) public returns(bool){
        require(balances[msg.sender] >= _value && _value > 0 );
        balances[msg.sender] = balances[msg.sender].sub(_value);
        balances[_to] = balances[_to].add(_value);
      //  emit Transfer(msg.sender, _to, _value);
        return true;
    }

    // Send _value amount of tokens from address _from to address _to
    // The transferFrom method is used for a withdraw workflow, allowing contracts to send
    // tokens on your behalf, for example to "deposit" to a contract address and/or to charge
    // fees in sub-currencies; the command should fail unless the _from account has
    // deliberately authorized the sender of the message via some mechanism; we propose
    // these standardized APIs for approval:
    function transferFrom(address _from, address _to, uint256 _value) public returns(bool){
        require(allowed[_from][msg.sender] >= _value && balances[_from] >= _value && _value > 0);
        balances[_from] = balances[_from].sub(_value);
        balances[_to] = balances[_to].add(_value);
        allowed[_from][msg.sender] = allowed[_from][msg.sender].sub(_value);
        //emit Transfer(_from, _to, _value);
        return true;
    }

    // Allow _spender to withdraw from your account, multiple times, up to the _value amount.
    // If this function is called again it overwrites the current allowance with _value.
    function approve(address _spender, uint256 _value) public returns(bool){
        allowed[msg.sender][_spender] = _value;
    //    emit Approval(msg.sender, _spender, _value);
        return true;
    }

    // Returns the amount which _spender is still allowed to withdraw from _owner
    function allowance(address _owner, address _spender) public view returns(uint256){
        return allowed[_owner][_spender];
    }
    //buy token using ether
    function buyToken() payable public returns(uint){
        uint tokens=msg.value/getTokenPrice();
        _transfer(getOwner(),msg.sender, tokens);
      //  emit Transfer(getOwner(),msg.sender, tokens);
        return tokens;
    }
    //lock balance
    function lockBalance(address from,address to,uint value) internal{
        require(balances[from]>=value&&value>0);
        balances[from]=balances[from].sub(value);
        lockBalances[from][to]=lockBalances[from][to].add(value);
    }
    //pay lock
    function lockPay(address from,address to,uint value)internal{
        require(lockBalances[from][to]>=value&&value>0);
        lockBalances[from][to]=lockBalances[from][to].sub(value);
        balances[to]=balances[to].add(value);
    }
    //release lock
    function lockRelease(address from,address to,uint value)internal{
        require(lockBalances[from][to]>=value&&value>0);
        lockBalances[from][to]=lockBalances[from][to].sub(value);
        balances[from]=balances[from].add(value);
    }
   // event Transfer(address indexed _from, address indexed _to, uint256 _value);
   // event Approval(address indexed _owner, address indexed _spender, uint256 _value);
}