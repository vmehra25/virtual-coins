## Virtual Coins

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
        <li><a href="#sample-output">Sample Output</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>


<!-- ABOUT THE PROJECT -->
## About The Project

This is a simulation of a virtual currency based on blockchain.

Here's why:
* Each block in the blockchain is mined by Miners who need to provide a proof of work.
* Each miner that mines the block first gets a reward in the form of virtual currency.
* Each transaction is a verified transaction.
* The sender signs the transaction with the private key and exposes public key so the transaction is verified.
* Going back through each block in the blockchain, we can check if every transaction is valid or not.
* Hash of each block depends on the hash of previous block.

### Sample Output

### Built With

* [Java](https://www.java.com/en/)

<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.<br>
You can tweak the values of NUM_BLOCKS and ZERO_COUNT for observation in Main.java.<br>
The same can be done for LOWER_TIME_BOUND, UPPER_TIME_BOUND and REWARD_COINS in Blockchain.java.

### Installation


1. Clone the repo
   ```sh
   git clone https://github.com/vmehra25/virtual-coins.git
   ```
2. Open the project in IntelliJ or any IDE.

3. Build the project and run Main.java


<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


<!-- CONTACT -->
## Contact

Vedant Mehra

[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/vmehra25/
