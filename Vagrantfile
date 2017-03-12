# Example 4
#
# Single box with Apache and sample static site installed via Puppet.
#
# NOTE: Make sure you have the trusty64 base box installed...

Vagrant.configure("2") do |config|
  config.vm.box = "boxcutter/ubuntu1604-desktop"
  config.vm.hostname = "myjava.box"
  config.vm.network :private_network, ip: "192.168.1.200"
  config.vm.provider :virtualbox do |vb|
    vb.customize [
      "modifyvm", :id,
      "--memory", "2000",
    ]
  end

  config.vm.provision :ansible_local do |ansible|
    ansible.playbook       = "example.yml"
    ansible.verbose        = true
    ansible.install        = true
    ansible.limit          = "all" # or only "nodes" group, etc.
    ansible.inventory_path = "inventory"
  end
end